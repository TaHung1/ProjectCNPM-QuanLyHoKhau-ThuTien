package controller.noptien;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.KhoanThuModel;
import models.HoKhauModel;
import models.NopTienModel;
import services.HoKhauService;
import services.NopTienService;

public class AddNopTien {
	@FXML
	private TextField tfTenKhoanThu;
	@FXML
	private TextField tfTenNguoiNop;
	private KhoanThuModel khoanThuModel;
	private HoKhauModel hoKhauModel;
	
	public void chooseKhoanThu() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/views/noptien/ChooseKhoanNop.fxml"));
		Parent home = loader.load(); 
        Stage stage = new Stage();
        stage.setScene(new Scene(home,800,600));
        stage.setResizable(false);
        stage.showAndWait();
        
        ChooseKhoanNop chooseKhoanNop = loader.getController();
        khoanThuModel = chooseKhoanNop.getKhoanthuChoose();
        if(khoanThuModel == null) return;
        
        tfTenKhoanThu.setText(khoanThuModel.getTenKhoanThu());
	}
	
	public void chooseNguoiNop() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/views/noptien/ChooseNguoiNop.fxml"));
		Parent home = loader.load(); 
        Stage stage = new Stage();
        stage.setTitle("Chọn hộ nộp");
        stage.setScene(new Scene(home,800,600));
        stage.setResizable(false);
        stage.showAndWait();
        
        ChooseNguoiNop chooseNguoiNop = loader.getController();
        hoKhauModel = chooseNguoiNop.getNhanKhauChoose();
        if(hoKhauModel == null) return;
        
        tfTenNguoiNop.setText(hoKhauModel.getTenChuHo());
	}
	
	public void addNopTien(ActionEvent event) throws ClassNotFoundException, SQLException {		
		if(tfTenKhoanThu.getText().length() == 0 || tfTenNguoiNop.getText().length() == 0) {
			Alert alert = new Alert(AlertType.WARNING, "Vui lòng nhập khoản nộp hợp lí!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
		} else {
			List<NopTienModel> listNopTien = new NopTienService().getListNopTien();
			for(NopTienModel nopTienModel : listNopTien) {
				if(nopTienModel.getIdNopTien() == hoKhauModel.getMaHo() // nếu trùng id và mã khoản thu thì nộp r
						&& nopTienModel.getMaKhoanThu() == khoanThuModel.getMaKhoanThu()) {
					Alert alert = new Alert(AlertType.WARNING, "Hộ này đã từng nộp khoản phí này!", ButtonType.OK);
					alert.setHeaderText(null);
					alert.showAndWait();
					return;
				}
			}
			if(hoKhauModel.getCheckNoptien().equals("Chưa nộp") || hoKhauModel.getCheckNoptien().equals("")) {
				hoKhauModel.setCheckNoptien(khoanThuModel.getTenKhoanThu());
			}
			else {
				hoKhauModel.setCheckNoptien(hoKhauModel.getCheckNoptien() + ", " + khoanThuModel.getTenKhoanThu());
			}
			new HoKhauService().update(hoKhauModel.getMaHo(), hoKhauModel.getDiaChi(), hoKhauModel.getCheckNoptien(), hoKhauModel.getTenChuHo());
			new NopTienService().add(new NopTienModel(hoKhauModel.getMaHo(),khoanThuModel.getMaKhoanThu()));
			
			
		}
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		stage.setTitle("Thêm nộp tiền");
		stage.setResizable(false);
        stage.close();
	}
}
