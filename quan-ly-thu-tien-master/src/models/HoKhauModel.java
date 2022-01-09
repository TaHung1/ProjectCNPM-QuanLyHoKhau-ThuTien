package models;

public class HoKhauModel {
	int maHo;
	int soThanhvien;
	String diaChi;
	String checkNoptien = "Chưa nộp";
	String tenChuHo;
	
	public String getTenChuHo() {
		return tenChuHo;
	}

	public void setTenChuHo(String tenChuHo) {
		this.tenChuHo = tenChuHo;
	}

	public String getCheckNoptien() {
		return checkNoptien;
	}

	public void setCheckNoptien(String checkNoptien) {
		this.checkNoptien = checkNoptien;
	}

	public HoKhauModel() {
		
	}
	
	public HoKhauModel(int soThanhVien, String diaChi) {
		this.soThanhvien = soThanhVien;
		this.diaChi = diaChi;
	}

	public HoKhauModel(int maHo ,int soThanhVien, String diaChi, String checkNoptien, String tenChuHo) {
		this.maHo=maHo;
		this.soThanhvien = soThanhVien;
		this.diaChi = diaChi;
		this.checkNoptien = checkNoptien;
		this.tenChuHo = tenChuHo;
	}
	public HoKhauModel(int maHo ,String diaChi,int soThanhVien, String tenChuHo) {
		this.maHo=maHo;
		this.soThanhvien = soThanhVien;
		this.diaChi = diaChi;
		this.tenChuHo = tenChuHo;
	}

	
	public int getMaHo() {
		return maHo;
	}

	public void setMaHo(int maHo) {
		this.maHo = maHo;
	}


	public int getSoThanhvien() {
		return soThanhvien;
	}

	public void setSoThanhvien(int soThanhvien) {
		this.soThanhvien = soThanhvien;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}
	
	
	
}
