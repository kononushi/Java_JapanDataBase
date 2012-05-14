package com.groupb.android.pdata;

public class JapanSetGet {

	int 番号;
	String 都道府県;
	String 県庁所在地;
	int 人口;
	double 面積;
	double 人口密度;
	int 市;
	int 区;
	int 町;
	int 村;

	String 都道府県庁;

	public void setRowid(int rowid){
		番号 = rowid;
	}
	public int getRowid(){
		return 番号;
	}

	public void setTodofuken(String todo){
		都道府県 = todo;
	}
	public String getTodofuken(){
		return 都道府県;
	}

	public void setKentyo(String kentyo){
		県庁所在地 = kentyo;
	}
	public String getKentyo(){
		return 県庁所在地;
	}

	public void setJinko(int jinko){
		人口 = jinko;
	}
	public int getJinko(){
		return 人口;
	}

	public void setMenseki(double menseki){
		面積 = menseki;
	}
	public double getMenseki(){
		return 面積;
	}

	public void setMitudo(double mitudo){
		人口密度 = mitudo;
	}
	public double getMitudo(){
		return 人口密度;
	}

	public void setSi(int si){
		市 = si;
	}
	public int getSi(){
		return 市;
	}

	public void setKu(int ku){
		区 = ku;
	}
	public int getKu(){
		return 区;
	}

	public void setTyo(int tyo){
		町 = tyo;
	}
	public int getTyo(){
		return 町;
	}

	public void setSon(int son){
		村 = son;
	}
	public int getSon(){
		return 村;
	}
}

