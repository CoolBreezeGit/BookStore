package com.coolbreeze.bookstore.cart.domain;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;


/*
 * 购物车实体
 */
public class Cart {
	
	//使用有顺序的使用bid作为key的map集合
	private Map<String,CartItem> cartItemMap=new LinkedHashMap<String, CartItem>();	
	
	public Map<String, CartItem> getCartItemMap() {
		return cartItemMap;
	}

	public void setCartItemMap(Map<String, CartItem> cartItemMap) {
		this.cartItemMap = cartItemMap;
	}

	/*
	 * 总计：total
	 * 必须使用Decimal精确运算
	 */
	public double getTotal(){
		//float total=0F;
		BigDecimal total=new BigDecimal("0");
		for(Entry<String,CartItem> entry : cartItemMap.entrySet()){
			BigDecimal subToal=new BigDecimal(entry.getValue().getSubTotal()+"");
			total=total.add(subToal);
		}
		
		return total.doubleValue();
	}

	/*
	 * 根据bid的key来添加条目
	 */
	public void addItem(String key,CartItem cartItem){
		if(!cartItemMap.keySet().contains(key)){	//如果不存在，则添加新条目
			cartItemMap.put(key, cartItem);
		}else{						//如果已经存在，则增加count数量
			CartItem item=cartItemMap.get(key);
			item.addCount(cartItem.getCount());
			cartItemMap.put(key, item);
		}
	}
	
	/*
	 * 根据bid的key删除购物车条目
	 */	
	public void deleteItem(String key){
		cartItemMap.remove(key);
	}
	
	
}
