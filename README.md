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
## 1. Sơ đồ ERD

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778812754/z7827349606226_84c74d3d73e19de247cd34b35a7d5931_mzqtny.jpg" width="70%"/>
</div>

## 2. Sơ đồ Class Diagram
<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778812755/z7827349606245_5bdc311fc829b6dd40573e0d22688f4b_ajdtud.jpg" width="70%"/>
</div>

# Giao diện các màn hình ứng dụng
## Xác thực người dùng

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729311/z7823866510991_b1327c3333873c6133fbcf53ac1135ff_w3dien.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729310/z7823866498281_204f5445039bab0605b7248326e98fd2_x9ngyj.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729310/z7823866372418_0e592f0f5a8e8c80fe9391f67a709b93_jwubop.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729310/z7823866268590_4d4ed6665007715a2ac4697bf8908358_uw3dio.jpg" width="23%"/>
</div>

## Trang chủ & Thông tin chi tiết thuốc

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729309/z7823867124443_e19f659a10275e970e0dcd6312f0353d_ef0wzw.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729310/z7823867199169_8d16476c7ac7547c5a7ad1363f97bc9b_oips47.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729310/z7823867409476_710a55fe72c4d456aedc84830d3c364e_mrck8j.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729310/z7823867350111_05a3a16a7bff64ce1dad5eb48f3191f8_brwjfa.jpg" width="23%"/>
</div>

## Tra cứu thuốc offline & mã QR & AI Gợi ý thuốc

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729310/z7823866197221_1e3074e1f8bf7df39e2eea5cacf08f9e_scn4cd.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729310/z7823866197220_76a9ea1b8971694c6ba06820b90d8557_rjjbs4.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729310/z7823867381621_aef75aebd1e19548101adffae6cb116a_wkrsod.jpg" width="23%"/>
</div>

## Quản lý kho thuốc

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729309/z7823867319322_03514053225b304ed1a34b49f294f6ea_m9weji.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729308/z7823867229389_9d13121d0fe6fa9f58584fcf596c9090_dmvxs7.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729310/z7823866197222_3c73acc7367ae99cb11ff4c2d7d8069a_diay8u.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729308/z7823867253726_8555510da3d5101ce05973680fcd95df_rwe1lh.jpg" width="23%"/>
</div>

## Quản lý tồn kho & giám sát tồn kho

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729309/z7823867079243_f138fb5811a4aff483282f3d524eed4d_tbfz8s.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729309/z7823867140856_2956b5e671a517a7e29deb7c9cceff93_ezxahu.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729310/z7823866324644_5336d6e9b0eefc9f3e08d3fb76a03355_dzj7ol.jpg" width="23%"/>
</div>

## Quản lý & tạo hóa đơn 

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729309/z7823867287919_9ece62341d4e155e370392ca84f98e5e_a0v1up.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729310/z7823866357494_d42b6a4e08f80cf52fbd7a2066424881_k1vqwf.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729308/z7823866860578_aeef900f2164b8e64b3f1cf26edb3b44_cdnqne.jpg" width="23%"/>
</div>

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729311/z7823866696775_feafd8605f346db9ba0a832631faae09_knhthb.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729308/z7823866997170_15e20d13eb9d748fe678e5a709745300_twpuig.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729308/z7823866919526_430f94114e1682d904b302e20ad65e3c_nbh02f.jpg" width="23%"/>
</div>

## Thông tin hồ sơ bệnh nhân

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729309/z7823867036185_2436475f1a947c03192788ea610812be_f1bbbx.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729311/z7823866767477_907f17bca57274b78a446d873e6ca909_jsexdo.jpg" width="23%"/>
</div>

## Lịch nhắc nhở & Quản lý mã khuyến mãi

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729308/z7823866890907_f49818b5cdbb387d54b778d1977ab2c8_atefij.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729311/z7823866601021_b1ad8f392bc72cdf02904483894ab687_itlezm.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729311/z7823866614521_8ba4cce3bb34e0f65e0ae68beb94f1a6_yh6qmt.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778729311/z7823866813721_abee998d5b2de0b4061299322c50c206_wrumsl.jpg" width="23%"/>
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

