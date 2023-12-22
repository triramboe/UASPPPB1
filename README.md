UAS PRAKTIKUM PEMROGRAMAN PERANGKAT BERGERAK 1
Dosen Pengampu : Ibu Margareta Hardiyanti, S.Kom.,M.Eng.
Asisten Dosen : Mbak Resti & Mas Nanang


Calorie tracker by Tri Rambu Nugroho Prasetyo

nama : Tri Rambu Nugroho Prasetyo
nim : 22/505631/SV/21850

Gambaran garis besar implementasi materi android:
- TabLayout
- SharedPreference
- Room
- RecyclerView
- Firebase
- BottomNavigationView
- Notification


Gambaran pengembangan fitur.

[sebelum login]

Pengguna aplikasi terdiri dari 2 role, yaitu admin dan user biasa. Role user bisa didaftarkan melalui aplikasi langsung, sementara role admin didaftarkan melalui firebase console langsung. Pengelolaan data user menggunakan firebase firestore, namun boleh juga dikombinasikan dengan firebase Auth. Fitur login dan register dapat dibuat dengan mengimplementasikan Tab Layout.
Implementasikan sharedpreference untuk menyimpan status login agar jika sudah login pengguna tidak perlu login lagi kecuali sudah di logout.
Pada form registrasi user harus memasukan informasi seperti tinggi badan, kalori target, dan berat badan seperti proyek UTS kemarin
 

[sesudah login untuk role admin]

Halaman untuk mengelola item makanan(CRUD), gunakan inputan atau data-data yang anda pakai ketika UTS.
Data item makanan ini dimasukan ke firebase firestore yang nantinya data tersebut akan tampil di halaman user biasa. Pada halaman ini anda bisa mengimplementasikan room ketika proses penambahan data tidak ada internet disimpan di room kemudian jika sudah ada internet dilakukan submit data ke firebase.
 

[sesudah login untuk role user biasa]

Menampilkan halaman dashboard dengan mengimplementasikan bottomnavigationview dengan susunan menu bebas, yang terdiri dari beranda, riwayat, dan Profil
Riwayat: datanya berdasar item makanan yang telah yang telah dikonsumsi dan terdapat action untuk melakukan update serta delete item.
Beranda: Dashboard yang menunjukkan sisa kalori harian dengan kalori acuan berdasarkan inputan user saat registrasi (untuk target kalori harian opsional dibuat fungsi untuk mengubah data kalori, namun jika kalori bersifat statis dan tidak bisa diubah melalui aplikasi juga diperbolehkan)
Profil: profil pengguna, menampilkan informasi nama, tinggi badan, dan berat badan .
Adapun flow saat menambahkan makanan, user menekan trigger berupa button atau menu yang mengarahkan ke halaman khusus untuk menampilkan daftar menu yang dimasukan admin di firestore. Pada halaman ini, terdapat search bar serta button lain yang berguna untuk inputan custom ( tidak dari daftar yang telah tersedia ). Ketika action menambahkan item dipilih maka akan muncul tampilan berupa form untuk mengkustomisasi takaran saji serta menambahkan keterangan waktu makan sebelum data disimpan pada penyimpanan Room dan akan ditampilkan pada menu riwayat. Lebih jelasnya dapat dilihat contoh user flow pada link berikut  https://www.figma.com/file/l80bVwekJBCNpuI1UTq8SQ/User-Flow-Calorie-Tracker?type=design&node-id=0%3A1&mode=design&t=iIqlBBhd8fWONEtI-1
Mengimplementasikan notifikasi bebas. Contoh: ketika jam tertentu ada pengingat jika kalori yang dikonsumsi belum mencapai target.


OVERVIEW TAMPILAN:
user :
![Screenshot 2023-12-22 205352](https://github.com/triramboe/UASPPPB1/assets/133026082/9757c010-8fb0-4242-8b57-273b627381a9)


admin :
![image](https://github.com/triramboe/UASPPPB1/assets/133026082/0961fa24-363d-447c-9505-cf159d84edcc)



LINK GDRIVE video demonstrasi:
https://drive.google.com/drive/folders/1JNQ82cNWbX2_VQTL3BnjuKSwFegkiFih?usp=sharing

