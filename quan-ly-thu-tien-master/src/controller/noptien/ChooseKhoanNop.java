package controller.noptien;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javafx.beans.property.ReadOnlyStringWrapper;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.KhoanThuModel;
import services.KhoanThuService;

public class ChooseKhoanNop implements Initializable {
	@FXML
	private TableView<KhoanThuModel> tvKhoanPhi;
	@FXML
	private TableColumn<KhoanThuModel, String> colMaKhoanPhi;
	@FXML
	private TableColumn<KhoanThuModel, String> colTenKhoanThu;
	@FXML
	private TableColumn<KhoanThuModel, String> colSoTien;
	@FXML
	private TableColumn<KhoanThuModel, String> colLoaiKhoanThu;
	@FXML
	private TextField tfSearch;
	@FXML
	private ComboBox<String> cbChooseSearch;
	private KhoanThuModel khoanthuChoose;
	private List<KhoanThuModel> listKhoanThu;
	private ObservableList<KhoanThuModel> listValueTableView;
	
	public KhoanThuModel getKhoanthuChoose() {
		return khoanthuChoose;
	}

	public void setKhoanthuChoose(KhoanThuModel khoanthuChoose) {
		this.khoanthuChoose = khoanthuChoose;
	}

	public void showKhoanThu() throws ClassNotFoundException, SQLException {
		listKhoanThu = new KhoanThuService().getListKhoanThu();
		listValueTableView = FXCollections.observableArrayList(listKhoanThu);

		// thiet lap cac cot cho table views
		colMaKhoanPhi.setCellValueFactory(new PropertyValueFactory<KhoanThuModel, String>("maKhoanThu"));
		colTenKhoanThu.setCellValueFactory(new PropertyValueFactory<KhoanThuModel, String>("tenKhoanThu"));
		colSoTien.setCellValueFactory(new PropertyValueFactory<KhoanThuModel, String>("soTien"));

		Map<Integer, String> mapLoaiKhoanThu = new TreeMap();
		mapLoaiKhoanThu.put(1, "B???t bu???c");
		mapLoaiKhoanThu.put(0, "T??? nguy???n");

		try {
			colLoaiKhoanThu
					.setCellValueFactory((CellDataFeatures<KhoanThuModel, String> p) -> new ReadOnlyStringWrapper(
							mapLoaiKhoanThu.get(p.getValue().getLoaiKhoanThu())));
		} catch (Exception e) {
			// TODO: handle exception
		}
		tvKhoanPhi.setItems(listValueTableView);

//		// thiet lap gia tri cho combobox
		ObservableList<String> listComboBox = FXCollections.observableArrayList("T??n kho???n thu", "M?? kho???n thu");
		cbChooseSearch.setValue("T??n kho???n thu");
		cbChooseSearch.setItems(listComboBox);
	}

	// Tim kiem khoan thu
	public void searchKhoanThu() {
		ObservableList<KhoanThuModel> listValueTableView_tmp = null;
		String keySearch = tfSearch.getText();

		// lay lua chon tim kiem cua khach hang
		SingleSelectionModel<String> typeSearch = cbChooseSearch.getSelectionModel();
		String typeSearchString = typeSearch.getSelectedItem();

		// tim kiem thong tin theo lua chon da lay ra
		switch (typeSearchString) {
		case "T??n kho???n thu": {
			// neu khong nhap gi -> thong bao loi
			if (keySearch.length() == 0) {
				tvKhoanPhi.setItems(listValueTableView);
				Alert alert = new Alert(AlertType.WARNING, "H??y nh???p v??o th??ng tin c???n t??m ki???m!", ButtonType.OK);
				alert.setHeaderText(null);
				alert.showAndWait();
				break;
			}

			int index = 0;
			List<KhoanThuModel> listKhoanThuModelsSearch = new ArrayList<>();
			for (KhoanThuModel khoanThuModel : listKhoanThu) {
				if (khoanThuModel.getTenKhoanThu().contains(keySearch)) {
					listKhoanThuModelsSearch.add(khoanThuModel);
					index++;
				}
			}
			listValueTableView_tmp = FXCollections.observableArrayList(listKhoanThuModelsSearch);
			tvKhoanPhi.setItems(listValueTableView_tmp);

			// neu khong tim thay thong tin can tim kiem -> thong bao toi nguoi dung khong
			// tim thay
			if (index == 0) {
				tvKhoanPhi.setItems(listValueTableView); // hien thi toan bo thong tin
				Alert alert = new Alert(AlertType.INFORMATION, "Kh??ng t??m th???y th??ng tin!", ButtonType.OK);
				alert.setHeaderText(null);
				alert.showAndWait();
			}
			break;
		}
		default: { // truong hop con lai : tim theo ma khoan thu
			// neu khong nhap gi -> thong bao loi
			if (keySearch.length() == 0) {
				tvKhoanPhi.setItems(listValueTableView);
				Alert alert = new Alert(AlertType.INFORMATION, "B???n c???n nh???p v??o th??ng tin t??m ki???m!", ButtonType.OK);
				alert.setHeaderText(null);
				alert.showAndWait();
				break;
			}

			// kiem tra thong tin tim kiem co hop le hay khong
			Pattern pattern = Pattern.compile("\\d{1,}");
			if (!pattern.matcher(keySearch).matches()) {
				Alert alert = new Alert(AlertType.WARNING, "B???n ph???i nh???p v??o 1 s???!", ButtonType.OK);
				alert.setHeaderText(null);
				alert.showAndWait();
				return;
			}

			for (KhoanThuModel khoanThuModel : listKhoanThu) {
				if (khoanThuModel.getMaKhoanThu() == Integer.parseInt(keySearch)) {
					listValueTableView_tmp = FXCollections.observableArrayList(khoanThuModel);
					tvKhoanPhi.setItems(listValueTableView_tmp);
					return;
				}
			}

			// khong tim thay thong tin -> thong bao toi nguoi dung
			tvKhoanPhi.setItems(listValueTableView);
			Alert alert = new Alert(AlertType.WARNING, "Kh??ng t??m th???y th??ng tin!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
		}
		}
	}

	public void xacnhan(ActionEvent event) {
		khoanthuChoose = tvKhoanPhi.getSelectionModel().getSelectedItem();
		setKhoanthuChoose(khoanthuChoose);
		
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			showKhoanThu();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
