package controller.nhankhau;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.HoKhauModel;
import models.NhanKhauModel;
import services.HoKhauService;
import services.NhanKhauService;

public class UpdateNhanKhau implements Initializable{
	private int maNhanKhau;
	@FXML
	private TextField tfMaNhanKhau;
	@FXML
	private TextField tfTuoi;
	@FXML
	private TextField tfTenNhanKhau;
	@FXML
	private TextField tfSoDienThoai;
	@FXML
	private TextField tfSoCMND;
	@FXML
	private ComboBox<String> tfTrangThai;
	@FXML
	private ComboBox<String> tfGioiTinh;
	@FXML
    private TextField tfMaHoKhau;

	private int maHoKhau;
	private NhanKhauModel nhanKhauModel;

	public NhanKhauModel getNhanKhauModel() {
		return nhanKhauModel;
	}

	public void setNhanKhauModel(NhanKhauModel nhanKhauModel) throws ClassNotFoundException, SQLException {
		this.nhanKhauModel = nhanKhauModel;

		maNhanKhau = nhanKhauModel.getId();
		maHoKhau = nhanKhauModel.getMaho();
		tfMaNhanKhau.setText(Integer.toString(maNhanKhau));
		tfMaHoKhau.setText(Integer.toString(maHoKhau));
		tfTuoi.setText(Integer.toString(nhanKhauModel.getTuoi()));
		tfTenNhanKhau.setText(nhanKhauModel.getTen());
		tfSoDienThoai.setText(nhanKhauModel.getSdt());
		tfSoCMND.setText(nhanKhauModel.getCmnd());
		

	}

	public void updateNhanKhau(ActionEvent event) throws ClassNotFoundException, SQLException {
		// khai bao mot mau de so sanh
		Pattern pattern;
		SingleSelectionModel<String> gioitinhSelection = tfGioiTinh.getSelectionModel();
		String gioitinh_tmp = gioitinhSelection.getSelectedItem();
		
		SingleSelectionModel<String> trangthaiSelection = tfTrangThai.getSelectionModel();
		String trangthai_tmp = trangthaiSelection.getSelectedItem();
		
		// kiem tra ten nhap vao
		// ten nhap vao la chuoi tu 1 toi 50 ki tu
		if (tfTenNhanKhau.getText().length() >= 50 || tfTenNhanKhau.getText().length() <= 1) {
			Alert alert = new Alert(AlertType.WARNING, "Hãy nhập vào 1 tên hợp lệ!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
			return;
		}


		// kiem tra tuoi nhap vao
		// tuoi nhap vao nhieu nhat la 1 so co 3 chu so
		pattern = Pattern.compile("\\d{1,3}");
		if (!pattern.matcher(tfTuoi.getText()).matches()) {
			Alert alert = new Alert(AlertType.WARNING, "Hãy nhập vào tuổi hợp lệ!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
			return;
		}

		// kiem tra cmnd nhap vao
		// cmnd nhap vao phai la mot day so tu 1 toi 20 so
		pattern = Pattern.compile("\\d{1,20}");
		if (!pattern.matcher(tfSoCMND.getText()).matches()) {
			Alert alert = new Alert(AlertType.WARNING, "Hãy nhập vào CMND hợp lệ!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
			return;
		}

		// kiem tra sdt nhap vao
		// SDT nhap vao phai khong chua chu cai va nho hon 15 chu so
		pattern = Pattern.compile("\\d{1,15}");
		if (!pattern.matcher(tfSoDienThoai.getText()).matches()) {
			Alert alert = new Alert(AlertType.WARNING, "Hãy nhập vào SĐT hợp lệ!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
			return;
		}

		// kiem tra maHo nhap vao
		// ma ho nhap vao phai khong chua chu cai va nho hon 11 chu so
		pattern = Pattern.compile("\\d{1,11}");
		if (!pattern.matcher(tfMaHoKhau.getText()).matches()) {
			Alert alert = new Alert(AlertType.WARNING, "Hãy nhập vào mã hộ hợp lệ!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
			return;
		}

		// kiem tra ma ho nhap vao da ton tai hay chua
		List<HoKhauModel> listHoKhauModels = new HoKhauService().getListHoKhau();
		long check = listHoKhauModels.stream()
				.filter(hokhau -> hokhau.getMaHo() == Integer.parseInt(tfMaHoKhau.getText())).count();
		if (check <= 0) {
			Alert alert = new Alert(AlertType.WARNING, "Không có hộ khẩu này!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
			return;
		}


		// ghi nhan gia tri ghi tat ca deu da hop le
		String tenString = tfTenNhanKhau.getText();
		int tuoiInt = Integer.parseInt(tfTuoi.getText());
		String cmndString = tfSoCMND.getText();
		String sdtString = tfSoDienThoai.getText();
		String trangthaiString = trangthai_tmp;
		String gioitinhString = gioitinh_tmp;
		int MaHo = Integer.parseInt(tfMaHoKhau.getText());
		// xoa di nhan khau hien tai va them vao nhan khau vua cap nhat
		new NhanKhauService().update(maNhanKhau, MaHo, cmndString, tenString, tuoiInt, sdtString, trangthaiString, gioitinhString);
		
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
		
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// thiet lap gia tri cho gioi tinh
		ObservableList<String> listComboBox = FXCollections.observableArrayList("Nam", "Nữ");
		tfGioiTinh.setValue("Nam");
		tfGioiTinh.setItems(listComboBox);
		// thiet lap gia tri cho trang thai
		ObservableList<String> listComboBox1 = FXCollections.observableArrayList("Tạm trú", "Tạm vắng", "Có mặt");
		tfTrangThai.setValue("Tạm trú");
		tfTrangThai.setItems(listComboBox1);
	}
}
