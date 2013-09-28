package org.jq.model;

public class SignTask extends BaseModel implements NetImage{

	private static final long serialVersionUID = 4586400159230078146L;

	public String appname;
	public String packagename;
	public String icon;
	public String apk;
	public double price;
	public double money;
	public double percent;
	public String intro;
	
	public String iconPth;
	public boolean hasInstalled=false;
	public int downpercent;

	public String apkPth;
	public long playtime=10L;
	@Override
	public String getImgUrl() {
		return icon;
	}
	@Override
	public void SetCachePth(String pth) {
		iconPth=pth;
	}
}
