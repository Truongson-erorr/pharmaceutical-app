# AI gợi ý thuốc theo triệu chứng

Hệ thống gợi ý thuốc được xây dựng dựa trên mô hình học máy, trong đó dữ liệu triệu chứng và thuốc được sử dụng để huấn luyện mô hình trí tuệ nhân tạo (AI). Sau quá trình huấn luyện, mô hình được chuyển đổi sang định dạng tương thích với thiết bị di động và tích hợp trực tiếp vào ứng dụng Android thông qua PyTorch Mobile.

# Quy trình xử lý trong hệ thống

- Dữ liệu thuốc và thông tin chi tiết được lưu trữ từ file CSV  
- Dữ liệu triệu chứng được chuẩn hóa và ánh xạ từ tiếng Việt sang tiếng Anh  
- Văn bản đầu vào được chuyển đổi thành vector đặc trưng dựa trên bộ từ vựng (vocab)  
- Vector này được đưa vào mô hình AI đã huấn luyện (.pt model)  
- Mô hình trả về xác suất dự đoán cho từng loại thuốc  
- Kết quả được xử lý bằng hàm softmax để chuẩn hóa xác suất  
- Hệ thống ánh xạ kết quả với cơ sở dữ liệu thuốc để hiển thị thông tin chi tiết  

Kết quả cuối cùng được hiển thị cho người dùng dưới dạng danh sách thuốc gợi ý, bao gồm:
- Tên thuốc
- Thành phần
- Công dụng
- Tác dụng phụ
- Hình ảnh
- Đánh giá liên quan

## Pipeline xử lý AI

- Input: triệu chứng người dùng nhập
- Tiền xử lý: chuẩn hóa + dịch Việt → Anh
- Vector hóa: chuyển text → bag-of-words vector
- Model inference: PyTorch Mobile (.pt file)
- Output: danh sách thuốc dự đoán
- Mapping: ánh xạ với database CSV thuốc
- UI: hiển thị top 5 kết quả phù hợp nhất

## Tích hợp trên Mobile

Mô hình AI sau khi huấn luyện được convert sang định dạng TorchScript (`.pt`) và load trực tiếp trong ứng dụng Android thông qua PyTorch Mobile. Điều này giúp hệ thống:

- Hoạt động offline không cần server AI
- Tăng tốc độ phản hồi
- Giảm độ trễ khi gợi ý thuốc


# Đối tượng sử dụng
- Chuyên gia / dược sĩ
- Nhân viên quản lý kho thuốc
- Cơ sở nhà thuốc
- Người dùng cần tra cứu và gợi ý thuốc

# Các tính năng chính

## Tra cứu thuốc
- Tìm kiếm thuốc theo tên
- Tìm kiếm theo từ khóa
- Hiển thị thông tin chi tiết: thành phần, công dụng, liều dùng, tác dụng phụ

## Tra cứu thuốc bằng mã QR
- Quét mã QR từ hóa đơn nhập/xuất
- Hoặc chọn ảnh QR từ thư viện
- Giải mã và truy xuất thông tin thuốc tương ứng
- Hiển thị chi tiết thuốc sau khi tra cứu

## Gợi ý thuốc bằng AI
- Nhập triệu chứng dạng văn bản
- Chuẩn hóa và xử lý dữ liệu đầu vào
- Chuyển đổi thành vector đặc trưng
- Dự đoán bằng mô hình AI (PyTorch)
- Hiển thị danh sách thuốc gợi ý

## Quản lý kho thuốc
- Xem danh sách thuốc trong kho
- Theo dõi số lượng tồn kho
- Theo dõi hạn sử dụng
- Cảnh báo thuốc sắp hết hoặc hết hạn

## Quản lý phiếu nhập kho
- Tạo phiếu nhập thuốc
- Thêm số lượng thuốc nhập
- Tự động tăng tồn kho sau khi nhập
- Lưu lịch sử nhập kho

## Quản lý phiếu xuất kho
- Tạo phiếu xuất thuốc
- Kiểm tra số lượng tồn trước khi xuất
- Cảnh báo khi tồn kho không đủ
- Cảnh báo khi số lượng sau xuất < 10
- Tự động tạo hồ sơ bệnh nhân khi xuất thành công

## Quản lý hồ sơ bệnh nhân
- Tự động tạo bệnh nhân sau khi xuất kho
- Lưu thông tin bệnh nhân
- Xem lịch sử sử dụng thuốc
- Theo dõi các lần xuất thuốc

## Hệ thống thông báo
- Thông báo nhập kho
- Thông báo xuất kho
- Thông báo cảnh báo tồn kho thấp
- Thông báo thuốc hết hạn

## Quản lý mã khuyến mãi
- Tạo mã khuyến mãi
- Cập nhật và chỉnh sửa mã
- Kích hoạt / vô hiệu hóa mã
- Theo dõi trạng thái sử dụng

## Lịch nhắc nhở
- Tạo lịch nhắc nhập kho
- Tạo lịch nhắc xuất kho
- Cảnh báo theo thời gian đã thiết lập
- Đánh dấu hoàn thành công việc
  
# Thiết kế cơ sở dữ liệu
## 1. ERD (Entity Relationship Diagram)

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778813573/Untitled_2_ag5xet.png" width="70%"/>
</div>

## 2. Class Diagram

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778813690/z7827427451734_6928703f8c92eafb8582cf9a0b88728d_yxeprc.jpg" width="70%"/>
</div>

# Giao diện các màn hình ứng dụng
## Xác thực người dùng

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349913/z7849639668265_4241076c43d06a8ad7c7ab86cccfc902_fbwk3f.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349911/z7849639654753_2b57fdf49dcbec47725fdf1d1b43ff82_op0uxc.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349912/z7849639654754_0d2a6b9d803f4af44a6e4f9090b2bbb6_c5wclp.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349911/z7849639639988_40890803e98766c9b5f199419bbb300d_yad2dq.jpg" width="23%"/>
</div>

## Trang chủ & Thông tin chi tiết thuốc

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349913/z7849657636751_e5868cb7af8679a1ffb57a6fe9c7efd1_ot7lru.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349912/z7849657636749_9adfd8c5e7a01840f08c9e70eae84aa7_qw2rje.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349913/z7849657677461_6e622168ed52c8c6d17110a953d10b46_iurava.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349912/z7849657649643_461cf6c3858bed82ae92e39c8f200552_kuwxso.jpg" width="23%"/>
</div>

## Tra cứu, tìm kiếm bằng QR và Offline

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349915/z7849699662012_1d50dfb36330d6ec48a0f18ba0d8b35e_mfpjcy.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349913/z7849663397485_54b8391704296e9315a37b1ecf4a7858_rmalo1.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349913/z7849663397486_d4d93eac8cfab75495ad6549733c3e48_zubd1m.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349913/z7849663414462_fdc6da997377694e2c0a90e5038b2d0c_klhcyt.jpg" width="23%"/>
</div>

## Gợi ý thuốc dựa trên triệu chứng

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349914/z7849667380548_3cd90efc8b2193874fb462c5f781c1d5_tfbxnn.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349914/z7849667380547_b93df0a39974e988cfec752b50c93ca9_ttpwnk.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349914/z7849667396410_18394e180b9f33e0bac238bd1c812f52_doih2b.jpg" width="23%"/>
</div>

## Quản lý kho thuốc 

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349915/z7849671895522_a31822fdbeb798a0938a81737d084084_rkndyd.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349914/z7849671868660_ec3ff06d8879b8a7516ea882c186cbb4_j4ucbp.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349914/z7849671868656_3eed0cd10a45960274e1c457e946e4d8_zkpnca.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349915/z7849671895521_363ad9afcbbc25a7a3b6493278844c2a_tnkbk6.jpg" width="23%"/>
</div>

### Quản lý & cảnh báo tồn kho

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779350694/z7849769132355_181ae1ff32da9099f4de7fcbbddf6e10_urqcza.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779350694/z7849769147429_3515c4f55b3cc7055bda702b13e603c4_wyuuy0.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779350694/z7849769132354_658d993c64a585b510cd42bfb107badb_ee99r2.jpg" width="23%"/>
</div>

## Quản lý & tạo hóa đơn 

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349913/z7849686511203_0f9b73b0c65f030dd7a4b5cfc5d01aa0_jdl5hr.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349911/z7849686260731_de4df71b85a3da6bc3a37444d819ed0b_qdaomy.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349912/z7849686307064_7af4a234b97b33b7bec5a1a9149628dd_qgxkw5.jpg" width="23%"/>
</div>

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349912/z7849686278126_6bc7ebfdd2b268dc2d4ae063db637224_on6xpn.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349912/z7849686478561_1fb6184ec4a8ec6c7ed8daedbce34160_xamqhl.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349912/z7849686368781_c592771f264eedb597a3427832652e3c_m9xof1.jpg" width="23%"/>
</div>

## Thông tin hồ sơ bệnh nhân

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349913/z7849688923459_8f39740b264855fd41b8a024bd75ab01_aeka6r.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349913/z7849688923458_c28529035fa9ebb82d40912be0955339_aiwpz4.jpg" width="23%"/>
</div>

## Lịch nhắc nhở & Quản lý mã khuyến mãi

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349913/z7849693094539_e57c63163d503bc41d3688787ca272c9_zokiht.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349914/z7849693094540_aa617768609f9ce537c486d4fcbcc169_tzsgne.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349914/z7849693094541_04723829238a5363c20a65b865c0c4a8_nc5yce.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349914/z7849693106233_a640d9c1c523e15f9fea737fa653d3a4_nmvtoo.jpg" width="23%"/>
</div>

## Thông báo nhập – xuất – cảnh báo tồn kho

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349914/z7849697811226_bc5b6fda17383aafa862c84aad1002bd_ocw1l1.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349915/z7849697811239_b8219405adab0b509b89f542357bcaa8_ieckh4.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349915/z7849697827787_c262f1d52904185e175e302dc68fb860_ath4xd.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1779349915/z7849697946626_2ae866d8aa74f23a97650055ff551454_rxqynf.jpg" width="23%"/>
</div>

# Cấu trúc thư mục chính
```
app/
├── authentication/
│
├── models/
│ ├── AppNotification.kt
│ ├── CategoryModel.kt
│ ├── ChatMessage.kt
│ ├── ExportReceipt.kt
│ ├── ImportReceipt.kt
│ ├── MedicineResult.kt
│ ├── Patient.kt
│ ├── ProductData.kt
│ ├── ProductModel.kt
│ ├── PromoCode.kt
│ ├── ReminderEntity.kt
│ ├── StockHistoryItem.kt
│ └── UserModel.kt
│
├── screens/
│ ├── category/
│ ├── chat_ai/
│ ├── drug_look_up/
│ ├── export_receipt/
│ ├── home/
│ ├── import_receipt/
│ ├── inventory/
│ ├── invoice/
│ ├── invoice_history/
│ ├── notifications/
│ ├── patient/
│ ├── product/
│ ├── promotion/
│ ├── reminder/
│ ├── search/
│ ├── stock/
│ └── suggest/
│
├── ui/
│ └── theme/
│
├── utils/
│
├── viewmodels/
│ ├── AISearchViewModel.kt
│ ├── AuthViewModel.kt
│ ├── CategoryViewModel.kt
│ ├── ChatViewModel.kt
│ ├── DrugLookupViewModel.kt
│ ├── ExportViewModel.kt
│ ├── ImportViewModel.kt
│ ├── InventoryViewModel.kt
│ ├── NotificationViewModel.kt
│ ├── PatientViewModel.kt
│ ├── ProductViewModel.kt
│ ├── PromoCodeViewModel.kt
│ ├── ReminderViewModel.kt
│ ├── StockHistoryViewModel.kt
│ ├── StockViewModel.kt
│ └── SuggestViewModel.kt
│
└── MainActivity.kt
```

# Công nghệ sử dụng

- Kotlin (Android Native)
- MVVM Architecture
- Firebase Authentication
- Firebase Firestore Database
- PyTorch (Machine Learning – gợi ý thuốc theo triệu chứng)
- QR Code Scanner (ML Kit / ZXing)
- Natural Language Processing (tiền xử lý triệu chứng)
- ViewModel + LiveData / StateFlow
- Clean Architecture (phân tầng rõ ràng)
- XML UI / Material Design Components

# Hướng phát triển

## Nâng cao AI gợi ý thuốc
- Mở rộng dataset triệu chứng và danh sách thuốc
- Tăng số lượng nhãn (labels) để mô hình học chi tiết hơn
- Cải thiện thuật toán vector hóa và xử lý ngôn ngữ tự nhiên
- Fine-tuning mô hình PyTorch để tăng độ chính xác dự đoán

## Tích hợp phân tích dữ liệu người dùng (Analytics)
- Theo dõi hành vi tìm kiếm thuốc của người dùng
- Thống kê loại thuốc được truy vấn nhiều nhất
- Phân tích xu hướng bệnh phổ biến theo thời gian
- Hỗ trợ ra quyết định nhập kho thông minh

## Phát triển chatbot tư vấn thuốc
- Xây dựng chatbot hỗ trợ hỏi đáp theo triệu chứng
- Tích hợp AI trả lời theo thời gian thực
- Kết hợp dữ liệu thuốc để tư vấn chính xác hơn
- Hỗ trợ đa ngôn ngữ trong tương lai

## Đồng bộ đa nền tảng
- Phát triển thêm Web Admin Dashboard
- Đồng bộ dữ liệu giữa Android – Web – Server
- Quản lý tập trung dữ liệu thuốc và bệnh nhân
- Hỗ trợ phân quyền người dùng (admin, chuyên gia, bệnh nhân)

## Tối ưu hệ thống kho bằng AI
- Dự đoán nhu cầu nhập thuốc theo lịch sử sử dụng
- Cảnh báo tồn kho thông minh theo ngưỡng động
- Tự động đề xuất nhập hàng khi sắp hết thuốc
- Giảm tình trạng thiếu hoặc dư tồn kho


## Tăng cường bảo mật hệ thống
- Mã hóa dữ liệu bệnh nhân và lịch sử giao dịch
- Bảo vệ API bằng authentication nâng cao
- Phân quyền truy cập theo vai trò người dùng
- Sao lưu dữ liệu định kỳ trên cloud

