# Test Kılavuzu

Bu klasör, EnUygunCase uygulamasının kapsamlı testlerini içerir.

## Test Yapısı

### 1. Repository Tests
- **ProductRepositoryTest**: API çağrıları ve veri işleme testleri
- **CartRepositoryTest**: Sepet işlemleri testleri
- **FavoriteRepositoryTest**: Favori işlemleri testleri

### 2. ViewModel Tests
- **ProductViewModelTest**: UI mantığı ve state yönetimi testleri

### 3. API Tests
- **ProductApiServiceTest**: Network çağrıları ve API entegrasyon testleri

## Test Çalıştırma

### Tüm Testleri Çalıştırma
```bash
./gradlew test
```

### Belirli Test Sınıfını Çalıştırma
```bash
./gradlew test --tests "com.example.enuyguncase.data.repository.ProductRepositoryTest"
```

### Test Suite Çalıştırma
```bash
./gradlew test --tests "com.example.enuyguncase.TestRunner"
```

## Test Kategorileri

### Unit Tests
- **Repository Tests**: Veri katmanı işlemlerini test eder
- **ViewModel Tests**: UI mantığını test eder
- **API Tests**: Network çağrılarını test eder

### Test Coverage
- API başarı/başarısızlık senaryoları
- Veritabanı işlemleri
- State yönetimi
- Error handling
- Edge cases

## Test Prensipleri

1. **Given-When-Then**: Her test bu yapıyı takip eder
2. **Mocking**: Dış bağımlılıklar mock'lanır
3. **Isolation**: Testler birbirinden bağımsızdır
4. **Readability**: Test isimleri açıklayıcıdır
5. **Coverage**: Tüm kritik işlevler test edilir

## Test Verileri

Testlerde kullanılan örnek veriler:
- MacBook Pro (1299.99 TL)
- iPhone 15 (999.99 TL)
- iPad Pro (799.99 TL)

## Hata Durumları

Testler şu hata durumlarını kontrol eder:
- Network hataları
- API hataları
- Veritabanı hataları
- Null/empty veriler
- Invalid input'lar 