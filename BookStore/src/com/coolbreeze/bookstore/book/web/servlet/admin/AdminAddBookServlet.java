package com.coolbreeze.bookstore.book.web.servlet.admin;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.HashAttributeSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.coolbreeze.bookstore.book.domain.Book;
import com.coolbreeze.bookstore.book.service.BookService;
import com.coolbreeze.bookstore.category.domain.Category;
import com.coolbreeze.bookstore.category.service.CategoryService;
import com.coolbreeze.commons.CommonUtils;


/*
 *  添加图书
 */
public class AdminAddBookServlet extends HttpServlet {

	private List<Category> categoryList=new CategoryService().queryAll();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			request.setCharacterEncoding("utf-8");
		
			//1.得到添加图书表单项目FileItem列表
			DiskFileItemFactory factory=new DiskFileItemFactory();
			ServletFileUpload sfu=new ServletFileUpload(factory);
			//设置图片最大为30kb
			sfu.setFileSizeMax(30*1024);
			List<FileItem> itemList=null;
			try {
				itemList=sfu.parseRequest(request);
			} catch (FileUploadException e) {
				//文件大小超出30kb，则返回错误信息给客户
				if(e instanceof FileUploadBase.FileSizeLimitExceededException){
					request.setAttribute("msg", "你上传的图片大于30kb！");
					request.setAttribute("categoryList", categoryList);
					request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request,response);
					return ;
				}
				throw new RuntimeException(e);
			}
			
			//2.得到bname，price，author，cid，author信息
			Map<String,String> itemMap=new HashMap<String,String>();
			Category category=new Category();
			for(FileItem item : itemList){
				if(item.isFormField()){
					if(item.getFieldName().equals("cid")){
						category.setCid(item.getString("utf-8"));						
					}else{
						itemMap.put(item.getFieldName(), item.getString("utf-8"));
					}					
				}	
			}
			//将普通表单项目映射为一个book对象
			Book book=CommonUtils.toBean(itemMap, Book.class);
			book.setBid(CommonUtils.uuid());
			book.setCategory(category);
			
			/*
			 * 3.处理图片上传
			 */
			//得到图书图片项
			FileItem imgItem=itemList.get(1);
			//得到图片名称
			String _imgName=imgItem.getName();
			
			//校验图片扩展名，必须为jpg，jpeg，png，bmp格式
			String _imgName1=_imgName.toLowerCase();
			if(!_imgName1.endsWith("jpg") && !_imgName1.endsWith("jpeg")
					&& !_imgName1.endsWith("png") && !_imgName1.endsWith("bmp")){				
				request.setAttribute("msg", "你上传的图片不符合格式！");
				request.setAttribute("categoryList", categoryList);
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request,response);
				return ;
			}
			
			int index=_imgName.lastIndexOf("\\");	//有些文件名会带路径\!
			if( index != -1){
				_imgName=_imgName.substring(index+1);
			}
			String imgName=CommonUtils.uuid()+"_"+_imgName; //添加uuid前缀，防止重名
			//创建图片文件路径：目录哈系打散
			int hashCode=imgName.hashCode();
			String hexHashCode=Integer.toHexString(hashCode);
			//图片根路径
			String root=request.getServletContext().getRealPath("/books_img");
			//图片打散目录：文件名第一第二个字符作为图片文件目录
			String dir=hexHashCode.charAt(0)+"/"+hexHashCode.charAt(1);
			File dirFile=new File(root,dir);
			//建立目录链
			dirFile.mkdirs();
			//把上传图片写入磁盘中
			File file=new File(dirFile,imgName);
			try {
				imgItem.write(file);		
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			/*
			 * 校验图片的尺寸，不能大于150*150
			 */
			Image image=new ImageIcon(file.getPath()).getImage();
			if(image.getHeight(null)>150 || image.getWidth(null)>150){
				//删除磁盘中的文件				
				file.delete();
				//dirFile.delete();
				//回显错误信息
				request.setAttribute("msg", "你上传的图片尺寸不能超过200 * 200！");
				request.setAttribute("categoryList", categoryList);
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request,response);
				return ;
			}
			
			//保存图片路径到book类中，以便写入数据库
			book.setImage("/books_img"+"/"+dir+"/"+imgName);			
			
			//写入数据库
			new BookService().addBook(book);
			
			request.setAttribute("msg", "添加图书成功！");
			request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request,response);
	}

}
