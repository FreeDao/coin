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
		return type+"(��"+needMoney+"��)";
	}
	
	public static PayType[] typeArr=new PayType[]{
		new PayType("20Q�ҳ�ֵ","������qq��",20),
		new PayType("50Q�ҳ�ֵ","������qq��",47),
		new PayType("30���ѳ�ֵ","�������ֻ���",30),
		new PayType("50���ѳ�ֵ","�������ֻ���",50),
		new PayType("֧����50��ֵ","������֧�����˺�",50),
		new PayType("֧����100��ֵ","������֧�����˺�",100),
		new PayType("֧����200��ֵ","������֧�����˺�",200),
	};
	
}
