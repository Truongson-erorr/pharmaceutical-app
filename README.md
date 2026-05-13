# 🤖 AI gợi ý thuốc theo triệu chứng

Hệ thống gợi ý thuốc được xây dựng dựa trên mô hình học máy, trong đó dữ liệu triệu chứng và thuốc được sử dụng để huấn luyện mô hình trí tuệ nhân tạo (AI). Sau quá trình huấn luyện, mô hình được chuyển đổi sang định dạng tương thích với thiết bị di động và tích hợp trực tiếp vào ứng dụng Android thông qua PyTorch Mobile.

# ⚙️ Quy trình xử lý trong hệ thống

- 📁 Dữ liệu thuốc và thông tin chi tiết được lưu trữ từ file CSV  
- 🌐 Dữ liệu triệu chứng được chuẩn hóa và ánh xạ từ tiếng Việt sang tiếng Anh  
- 🔤 Văn bản đầu vào được chuyển đổi thành vector đặc trưng dựa trên bộ từ vựng (vocab)  
- 🧠 Vector này được đưa vào mô hình AI đã huấn luyện (.pt model)  
- 📊 Mô hình trả về xác suất dự đoán cho từng loại thuốc  
- 📈 Kết quả được xử lý bằng hàm softmax để chuẩn hóa xác suất  
- 🔗 Hệ thống ánh xạ kết quả với cơ sở dữ liệu thuốc để hiển thị thông tin chi tiết  

Kết quả cuối cùng được hiển thị cho người dùng dưới dạng danh sách thuốc gợi ý, bao gồm:
- Tên thuốc
- Thành phần
- Công dụng
- Tác dụng phụ
- Hình ảnh
- Đánh giá liên quan

## ⚙️ Pipeline xử lý AI

- Input: triệu chứng người dùng nhập
- Tiền xử lý: chuẩn hóa + dịch Việt → Anh
- Vector hóa: chuyển text → bag-of-words vector
- Model inference: PyTorch Mobile (.pt file)
- Output: danh sách thuốc dự đoán
- Mapping: ánh xạ với database CSV thuốc
- UI: hiển thị top 5 kết quả phù hợp nhất

## 📱 Tích hợp trên Mobile

Mô hình AI sau khi huấn luyện được convert sang định dạng TorchScript (`.pt`) và load trực tiếp trong ứng dụng Android thông qua PyTorch Mobile. Điều này giúp hệ thống:

- Hoạt động offline không cần server AI
- Tăng tốc độ phản hồi
- Giảm độ trễ khi gợi ý thuốc


# 🎯 Đối tượng sử dụng
- Chuyên gia / dược sĩ
- Nhân viên quản lý kho thuốc
- Cơ sở nhà thuốc
- Người dùng cần tra cứu và gợi ý thuốc

# 🚀 Các tính năng chính

## 🔍 Tra cứu thuốc
- Tìm kiếm thuốc theo tên
- Tìm kiếm theo từ khóa
- Hiển thị thông tin chi tiết: thành phần, công dụng, liều dùng, tác dụng phụ

## 📷 Tra cứu thuốc bằng mã QR
- Quét mã QR từ hóa đơn nhập/xuất
- Hoặc chọn ảnh QR từ thư viện
- Giải mã và truy xuất thông tin thuốc tương ứng
- Hiển thị chi tiết thuốc sau khi tra cứu

## 🤖 Gợi ý thuốc bằng AI
- Nhập triệu chứng dạng văn bản
- Chuẩn hóa và xử lý dữ liệu đầu vào
- Chuyển đổi thành vector đặc trưng
- Dự đoán bằng mô hình AI (PyTorch)
- Hiển thị danh sách thuốc gợi ý

## 📦 Quản lý kho thuốc
- Xem danh sách thuốc trong kho
- Theo dõi số lượng tồn kho
- Theo dõi hạn sử dụng
- Cảnh báo thuốc sắp hết hoặc hết hạn

## 📥 Quản lý phiếu nhập kho
- Tạo phiếu nhập thuốc
- Thêm số lượng thuốc nhập
- Tự động tăng tồn kho sau khi nhập
- Lưu lịch sử nhập kho

## 📤 Quản lý phiếu xuất kho
- Tạo phiếu xuất thuốc
- Kiểm tra số lượng tồn trước khi xuất
- Cảnh báo khi tồn kho không đủ
- Cảnh báo khi số lượng sau xuất < 10
- Tự động tạo hồ sơ bệnh nhân khi xuất thành công

## 👤 Quản lý hồ sơ bệnh nhân
- Tự động tạo bệnh nhân sau khi xuất kho
- Lưu thông tin bệnh nhân
- Xem lịch sử sử dụng thuốc
- Theo dõi các lần xuất thuốc

## 🔔 Hệ thống thông báo
- Thông báo nhập kho
- Thông báo xuất kho
- Thông báo cảnh báo tồn kho thấp
- Thông báo thuốc hết hạn

## 🎟️ Quản lý mã khuyến mãi
- Tạo mã khuyến mãi
- Cập nhật và chỉnh sửa mã
- Kích hoạt / vô hiệu hóa mã
- Theo dõi trạng thái sử dụng

## ⏰ Lịch nhắc nhở
- Tạo lịch nhắc nhập kho
- Tạo lịch nhắc xuất kho
- Cảnh báo theo thời gian đã thiết lập
- Đánh dấu hoàn thành công việc
# 📸 Giao diện các màn hình ứng dụng

## 🔐 Xác thực người dùng

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656795/z7821020507379_9640919ee2f397669222a4b3b757ead8_fafkmu.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656796/z7821020677427_24554351d4c36c75d8d06f5e5de6d584_vvivym.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656795/z7821020567625_4aedf3f5a42c6a38c8bab4cfe37e80f4_ennype.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656795/z7821020536581_2fc55daa2fd557a7d451d47a880ea36f_qh2nu8.jpg" width="23%"/>
</div>

## 🏠 Trang chủ và quản lý thông tin thuốc

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656794/z7821021059837_07d3db75331016f4afc4e648e77b8ae8_ibhevw.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656794/z7821021026187_35bc9f3a3b8709175b6bdee1c8f2888a_tc31fu.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656794/z7821021141003_0582f8ab1a0aaed85f08f6bf181147f6_a8diiq.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656794/z7821021085914_f4fa4c8dbb20e72a09d7c26f25b895c2_or6jen.jpg" width="23%"/>
</div>

## 📷 Tra cứu thuốc bằng mã QR & 🤖 Gợi ý thuốc

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656795/z7821020376551_a797f915433549de2f5e74bf0ca3603d_v2wtkv.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656794/z7821020489095_bcfcf5717456729cb078d394a391bb19_p0ww7u.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656796/z7821020766158_dda39e468adc1631d245d969e1d8840e_jfecnd.jpg" width="23%"/>
</div>

## 📦 Quản lý kho thuốc

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656793/z7821020994604_99d2439c3bcf7bf8052b2393f6663d6b_tdylni.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778657646/z7821020359550_148ee07f541c6fb23ca95f3f5c68a42f_pgksbm.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778657646/z7821020376550_411170016ee64c4a51a2c1ae7fe1c0de_phm8o9.jpg" width="23%"/>
</div>

## 📦 Quản lý tồn kho

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656793/z7821020964598_570ed927f8ff40e055fbe83c614c5b48_qlh0so.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778659201/z7821020460595_c104a17c10e7be5fd3f4876f672fc510_llyczy.jpg" width="23%"/>
</div>

## 🧾 Quản lý hóa đơn

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656794/z7821021085913_4cf814e135d2038d766dff277bf6353c_ke7bwu.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656795/z7821020376555_725239b4f9ff85cbbfa322795ce958ac_j1qzwl.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656793/z7821020826393_20e2d787a1fae6e4428dbce7ac8ec8ee_et1ela.jpg" width="23%"/>
</div>

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656795/z7821020408944_59e61a46b6cbac6b881a0a2ccbbd0cc4_cgekgc.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656793/z7821020932127_ae922da1b5972bca8569370bc0063129_g2l4oa.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656793/z7821020871548_96c17c0376be0c810389dd2d34285694_vncna1.jpg" width="23%"/>
</div>

## 👤 Thông tin hồ sơ bệnh nhân

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656795/z7821020629929_23e6cacd4e231943c8297d63ffbe24b7_lsatsm.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656796/z7821020748767_1abdbcdd5021b1b6c101b5abb10b07b7_jxn6in.jpg" width="23%"/>
</div>

## ⏰ Lịch nhắc nhở & 🎟️ Quản lý mã khuyến mãi

<div align="left">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656793/z7821020931877_c9ec29c511e0d4cd0b9285e22e31994b_rvwf3v.jpg" width="23%"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1778656795/z7821020715361_8c635994ea2698700553605606bf0668_mnb7cs.jpg" width="23%"/>
</div>

# 📁 Cấu trúc thư mục chính
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

# 🛠️ Công nghệ sử dụng

- 🟡 Kotlin (Android Native)
- 🧩 MVVM Architecture
- 🔥 Firebase Authentication
- ☁️ Firebase Firestore Database
- 🤖 PyTorch (Machine Learning – gợi ý thuốc theo triệu chứng)
- 📷 QR Code Scanner (ML Kit / ZXing)
- 🧠 Natural Language Processing (tiền xử lý triệu chứng)
- 📦 ViewModel + LiveData / StateFlow
- 🗂️ Clean Architecture (phân tầng rõ ràng)
- 🎨 XML UI / Material Design Components

# 💡 Hướng phát triển

- 🚀 Nâng cao độ chính xác mô hình AI gợi ý thuốc bằng cách mở rộng dataset triệu chứng và thuốc
- 📊 Tích hợp hệ thống phân tích dữ liệu người dùng (analytics)
- 🤖 Phát triển chatbot tư vấn thuốc thông minh theo thời gian thực
- ☁️ Đồng bộ dữ liệu đa nền tảng (Android – Web – Admin Dashboard)
- 🔔 Tối ưu hệ thống cảnh báo tồn kho bằng AI dự đoán nhu cầu nhập thuốc
- 📱 Phát triển phiên bản iOS hoặc cross-platform
- 🔐 Tăng cường bảo mật dữ liệu bệnh nhân và lịch sử sử dụng thuốc

