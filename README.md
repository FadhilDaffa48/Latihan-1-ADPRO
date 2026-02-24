# REFLECTION 1
Terkait Penerapan Clean code saya telah menerapkan prinsip-prinsipnya, seperti Meaningful Names. Saya memastikan penamaan
variabel itu mudah dimengerti dan maknanya cukup menggambarkan apa yang ia lakukan tanpa menambah komentar. Selain itu 
saya juga mempelajari tentang Functions yang baik dimana functions pada kode saya hanya melakukan 1 hal dan tidak ada hal lain.
Pemisahan logika antara controller, service, dan repository juga membantu menjaga organisasi kode. Untuk secure coding sendiri saya mempelajari
pentingnya validasi data yang masuk dengan menambahkan hiddden input untuk productId agar menjaga id produk. Tetapi saya rasa program saya
sekarang memiliki kekurangan fatal yaitu ia mudah kena spam oleh suatu bot, maka kita perlu menambah validasi yang telah saya ulik sejauh ini
kita bisa menambahkan semacam dependency validasi pada grade yaitu

implementation("org.springframework.boot:spring-boot-starter-validation")

lalu menambahkan decorator seperti @NotBlank @Min dan @Valid pada data yang rentan terkena dampak spam bot serta menambahkan pesan error pada HTMLnya
Contoh:
// Illustrasi kode saja

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
// import lainnya

@Controller
@RequestMapping("/product")
public class ProductController {

    @PostMapping("/create")
    public String createProductPost(@Valid @ModelAttribute Product product, BindingResult bindingResult, Model model) {
        // cek apakah ada error validasi
        
        if (bindingResult.hasErrors()) {
            return "CreateProduct"; // Tetap di halaman form jika ada error
        }
        service.create(product);
        return "redirect:list";
    }
    @PostMapping("/edit")
    public String editProductPost(@Valid @ModelAttribute Product product, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "EditProduct"; // Tetap di halaman edit jika ada error
        }
        service.edit(product);
        return "redirect:list";
    }
}

package id.ac.ui.cs.advprog.eshop.model;
import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class Product {
    
    private String productId;
    @NotBlank(message = "Nama produk tidak boleh kosong!")
    private String productName;
    @Min(value = 1, message = "Jumlah minimal adalah 1")
    private int productQuantity;
}

# REFLECTION 2
1. Setelah melakukan unit test saya merasa semakin yakin bahwa kode saya tidak mengalami suatu kerusakan logika
   bahkan setelah saya lakukan berbagai macam refactoring dan merging menggunakan git merge, unit test tetap berjalan baik
   dan ini juga memudahkan saya dalam maintenance kode kedepannya karena dengan unit test sendiri saya bisa menambahkan fitur baru
   dan melakukan pengecekan hanya dengan tes yang sudah saya buat. Untuk jumlah unit test sendiri sebenarnya tidak berpengaruh karena
   tergantung pada koompleksitas program yang sedang dibuat yang jelas harus memiliki Positive case, Negative case, edge case sehingga
   semua jenis penggunaan user tercover. nah dengan 100% code coverage sendiri tidak menjamin kode tidak memiliki error karena kita bisa
   saja salah melakukan assertion ataupun kekurangan skenario.

2. Jika membuat functional test baru dengan duplikasi setup CreateProductFunctionalTest.java, maka akan menyebabkan kode jadi kotor
   pertama kita akan memiliki kode dengan setup yang sama persis pada dua file berbeda sehingga tidak efisien. kedua perawatan jadi semakin sulit karena
   jika misalkan ada 1 bagian yg perlu diubah pada setup maka kita harus mengubah di semua file dengan setup yang sama satu per satu. dan juga kode jadi
   semakin sulit dibaca karena konfigurasi yang berulang ulang malah dapat berujung mengalihkan fokus.
   Untuk perbaikan sendiri dapat menggunakan inheritance misalnya menggunakan satu base test case dan fokuskan semua konfigurasi ke base tersebut
   kemudian CreateProductFunctionalTest dan duplikatnya dapat extend ke base yang baru tersebut saja

# Module 2

# Reflection
1. Pada modul ini, awalnya saya menggunakan SonarQube sebagai alat untuk melakukan analisis kualitas kode karena fitur analisisnya cukup komprehensif dan banyak digunakan dalam praktik industri. Namun, dalam implementasinya SonarQube mengharuskan proses merging ke branch main agar analisis berjalan secara optimal, sedangkan ketentuan modul tidak mengizinkan perubahan langsung pada branch utama sebelum seluruh tahapan selesai. Kondisi tersebut membuat pendekatan ini kurang sesuai dengan kebutuhan tugas, sehingga saya memutuskan untuk beralih menggunakan PMD yang dapat dijalankan langsung melalui workflow CI tanpa perlu melakukan merge terlebih dahulu. Setelah PMD berhasil diintegrasikan ke dalam pipeline, ditemukan beberapa permasalahan code quality pada kelas EditDeleteFunctionalTest dan ProductRepositoryTest. Permasalahan utama yang terdeteksi adalah adanya duplikasi string literal yang digunakan berulang kali dalam beberapa method pengujian. Duplikasi tersebut melanggar prinsip clean code dan terdeteksi oleh aturan AvoidDuplicateLiterals pada PMD. Untuk mengatasi masalah ini, saya melakukan refactoring dengan mengganti string yang berulang menjadi satu variabel konstanta (private static final) sehingga tidak ada lagi literal yang ditulis berkali-kali. Perubahan ini membuat kode lebih terstruktur, lebih mudah dipelihara, dan sesuai dengan standar kualitas yang ditetapkan. Setelah refactoring dilakukan, hasil analisis PMD menunjukkan tidak ada lagi pelanggaran aturan, dan workflow CI dapat berjalan dengan sukses tanpa error.

2. Implementasi workflow yang saya buat telah memenuhi konsep Continuous Integration karena setiap push dan pull request secara otomatis memicu proses build serta menjalankan seluruh unit test yang ada di dalam proyek. Selain itu, workflow juga menjalankan analisis statis menggunakan PMD sehingga kualitas kode diperiksa secara otomatis setiap kali terjadi perubahan. Dengan pendekatan ini, setiap kontribusi kode langsung divalidasi tanpa perlu pengecekan manual, sehingga risiko integrasi kode yang bermasalah dapat diminimalkan. Proses CI tersebut memastikan bahwa kode yang digabungkan ke repository tetap konsisten, teruji, dan memenuhi standar kualitas yang telah ditetapkan. Hal ini sejalan dengan prinsip Continuous Integration yang menekankan integrasi kode secara berkelanjutan dan terotomatisasi. Untuk aspek Continuous Deployment, sistem telah dikonfigurasi agar secara otomatis melakukan deployment ke Render setiap kali terdapat push ke branch main. Mekanisme ini memungkinkan aplikasi langsung diperbarui tanpa intervensi manual setelah perubahan dikirim ke branch utama. Namun demikian, workflow CD saat ini belum memiliki dependensi langsung terhadap hasil pipeline CI, sehingga deployment tetap dapat berjalan meskipun ada kemungkinan kegagalan pada tahapan test atau analisis jika tidak dikontrol melalui branch protection. Oleh karena itu, meskipun implementasi ini sudah mengarah pada praktik Continuous Deployment, sistem masih dapat ditingkatkan dengan memastikan deployment hanya dilakukan setelah seluruh tahapan CI berhasil dan dengan menerapkan perlindungan branch untuk menjaga stabilitas versi produksi.