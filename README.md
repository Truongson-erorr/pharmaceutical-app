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

