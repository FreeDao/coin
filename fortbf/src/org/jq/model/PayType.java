package org.jq.model;


public class PayType extends BaseModel{

	private static final long serialVersionUID = 1729357716220683486L;

	public PayType(){
		super();
	}
	
	public PayType(String type, String intro, double needMoney) {
		super();
		this.type = type;
		this.intro = intro;
		this.needMoney = needMoney;
	}

	public String type;
	public String intro;
	public double needMoney;
	
	public String message;
	
	@Override
	public String toString() {
		return type+"(需"+needMoney+"￥)";
	}
	
	public static PayType[] typeArr=new PayType[]{
		new PayType("20Q币充值","请输入qq号",20),
		new PayType("50Q币充值","请输入qq号",47),
		new PayType("30话费充值","请输入手机号",30),
		new PayType("50话费充值","请输入手机号",50),
		new PayType("支付宝50充值","请输入支付宝账号",50),
		new PayType("支付宝100充值","请输入支付宝账号",100),
		new PayType("支付宝200充值","请输入支付宝账号",200),
	};
	
}
