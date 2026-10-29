import os
import docx
from docx import Document
from docx.shared import Inches, Pt, RGBColor, Cm
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_ALIGN_VERTICAL
from docx.oxml import OxmlElement, parse_xml
from docx.oxml.ns import nsdecls, qn

def set_cell_background(cell, fill_hex):
    tcPr = cell._tc.get_or_add_tcPr()
    shd = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{fill_hex}"/>')
    tcPr.append(shd)

def set_cell_margins(cell, top=100, bottom=100, left=150, right=150):
    tcPr = cell._tc.get_or_add_tcPr()
    tcMar = OxmlElement('w:tcMar')
    for m, val in [('top', top), ('bottom', bottom), ('left', left), ('right', right)]:
        node = OxmlElement(f'w:{m}')
        node.set(qn('w:w'), str(val))
        node.set(qn('w:type'), 'dxa')
        tcMar.append(node)
    tcPr.append(tcMar)

def set_table_borders(table, color="CCCCCC", sz="4", val="single"):
    tblPr = table._tbl.tblPr
    borders = parse_xml(
        f'<w:tblBorders {nsdecls("w")}>'
        f'<w:top w:val="{val}" w:sz="{sz}" w:space="0" w:color="{color}"/>'
        f'<w:bottom w:val="{val}" w:sz="{sz}" w:space="0" w:color="{color}"/>'
        f'<w:left w:val="none"/>'
        f'<w:right w:val="none"/>'
        f'<w:insideH w:val="{val}" w:sz="{sz}" w:space="0" w:color="{color}"/>'
        f'<w:insideV w:val="none"/>'
        f'</w:tblBorders>'
    )
    tblPr.append(borders)

def create_document():
    doc = Document()

    # Set Margins (Standard Academic Report: Top 3cm, Left 3.5cm, Bottom 3cm, Right 3cm)
    for section in doc.sections:
        section.top_margin = Cm(3.0)
        section.bottom_margin = Cm(3.0)
        section.left_margin = Cm(3.5)
        section.right_margin = Cm(3.0)
        section.page_width = Cm(21.0)
        section.page_height = Cm(29.7) # A4

    # Base Normal Style
    normal_style = doc.styles['Normal']
    normal_style.font.name = 'Times New Roman'
    normal_style.font.size = Pt(12)
    normal_style.font.color.rgb = RGBColor(0x22, 0x22, 0x22)
    normal_style.paragraph_format.line_spacing = 1.35
    normal_style.paragraph_format.space_after = Pt(4)

    # -------------------------------------------------------------
    # COVER PAGE
    # -------------------------------------------------------------
    p_cov_type = doc.add_paragraph()
    p_cov_type.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_cov_type = p_cov_type.add_run("LAPORAN PERTANGGUNGJAWABAN (LPJ)\nTUGAS AKHIR PRAKTIK PEMROGRAMAN PERANGKAT BERGERAK")
    r_cov_type.font.size = Pt(14)
    r_cov_type.bold = True
    r_cov_type.font.color.rgb = RGBColor(0x11, 0x11, 0x11)
    p_cov_type.paragraph_format.space_after = Pt(18)

    p_cov_title = doc.add_paragraph()
    p_cov_title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_cov_title = p_cov_title.add_run(
        "PENGEMBANGAN APLIKASI E-COMMERCE FASHION & LIFESTYLE \"SHOPSEY\"\n"
        "BERBASIS ANDROID NATIVE DENGAN JETPACK COMPOSE DAN CLEAN UI STATE"
    )
    r_cov_title.font.size = Pt(16)
    r_cov_title.bold = True
    r_cov_title.font.color.rgb = RGBColor(0x00, 0x33, 0x66) # Elegant navy accent
    p_cov_title.paragraph_format.space_after = Pt(28)

    p_cov_sub = doc.add_paragraph()
    p_cov_sub.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_cov_sub = p_cov_sub.add_run(
        "Diajukan untuk Memenuhi Syarat Kelulusan Tugas Praktik Mata Pelajaran\n"
        "Pemrograman Perangkat Bergerak (Mobile Programming - Android Studio)"
    )
    r_cov_sub.font.size = Pt(11)
    r_cov_sub.font.italic = True
    p_cov_sub.paragraph_format.space_after = Pt(45)

    # Logo placeholder or text
    p_cov_logo = doc.add_paragraph()
    p_cov_logo.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_cov_logo = p_cov_logo.add_run("[ LOGO INSTANSI / SEKOLAH ]")
    r_cov_logo.font.size = Pt(12)
    r_cov_logo.font.color.rgb = RGBColor(0x88, 0x88, 0x88)
    p_cov_logo.paragraph_format.space_after = Pt(55)

    p_cov_auth = doc.add_paragraph()
    p_cov_auth.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_cov_auth = p_cov_auth.add_run("Disusun Oleh:\n")
    r_cov_auth.font.size = Pt(12)
    r_cov_name = p_cov_auth.add_run("RAFI ARDIANSYAH\n")
    r_cov_name.font.size = Pt(13)
    r_cov_name.bold = True
    r_cov_nis = p_cov_auth.add_run("NISN / NIM: [...................................]\n")
    r_cov_nis.font.size = Pt(11)
    p_cov_auth.paragraph_format.space_after = Pt(40)

    p_cov_inst = doc.add_paragraph()
    p_cov_inst.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_cov_inst = p_cov_inst.add_run(
        "KOMPETENSI KEAHLIAN SISTEM INFORMASI, JARINGAN, DAN APLIKASI (SIJA)\n"
        "SMK NEGERI 2 YOGYAKARTA\n"
        "TAHUN AJARAN 2026"
    )
    r_cov_inst.font.size = Pt(12)
    r_cov_inst.bold = True
    p_cov_inst.paragraph_format.space_after = Pt(0)

    doc.add_page_break()

    # -------------------------------------------------------------
    # LEMBAR PENGESAHAN
    # -------------------------------------------------------------
    p_peng_title = doc.add_paragraph()
    p_peng_title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r = p_peng_title.add_run("LEMBAR PENGESAHAN LAPORAN TUGAS PRAKTIK")
    r.font.size = Pt(14)
    r.bold = True
    p_peng_title.paragraph_format.space_after = Pt(20)

    p_peng_body = doc.add_paragraph()
    p_peng_body.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    p_peng_body.add_run(
        "Laporan Pertanggungjawaban (LPJ) Tugas Akhir Praktik Pemrograman Perangkat Bergerak dengan judul:\n\n"
    )
    r_title_inline = p_peng_body.add_run(
        "\"PENGEMBANGAN APLIKASI E-COMMERCE FASHION & LIFESTYLE 'SHOPSEY' "
        "BERBASIS ANDROID NATIVE DENGAN JETPACK COMPOSE DAN CLEAN UI STATE\"\n\n"
    )
    r_title_inline.bold = True
    p_peng_body.add_run(
        "Telah diperiksa, diuji dalam demonstrasi fungsional aplikasi Android Studio, "
        "dan disetujui serta disahkan oleh Guru Pembimbing / Penguji pada:\n"
    )
    p_peng_body.paragraph_format.space_after = Pt(12)

    p_peng_meta = doc.add_paragraph()
    p_peng_meta.add_run("Hari, Tanggal\t: ........................................................\n")
    p_peng_meta.add_run("Tempat\t\t: Laboratorium Komputer / Ruang Praktik SIJA SMKN 2 Yogyakarta\n")
    p_peng_meta.add_run("Status Pengujian\t: LULUS DENGAN PREDIKAT BAIK\n")
    p_peng_meta.paragraph_format.space_after = Pt(40)

    # Signature Table
    sig_table = doc.add_table(rows=3, cols=2)
    sig_table.alignment = WD_TABLE_ALIGNMENT.CENTER
    sig_table.autofit = False

    sig_table.rows[0].cells[0].paragraphs[0].text = "Mengetahui / Menyetujui,\nGuru Pembimbing Praktik"
    sig_table.rows[0].cells[1].paragraphs[0].text = "Yogyakarta, 28 Oktober 2026\nPenyusun / Siswa"

    sig_table.rows[1].cells[0].paragraphs[0].text = "\n\n\n\n"
    sig_table.rows[1].cells[1].paragraphs[0].text = "\n\n\n\n"

    sig_table.rows[2].cells[0].paragraphs[0].text = "(........................................................)\nNIP. ................................................."
    sig_table.rows[2].cells[1].paragraphs[0].text = "Rafi Ardiansyah\nNISN. ..............................................."

    for row in sig_table.rows:
        for cell in row.cells:
            cell.paragraphs[0].alignment = WD_ALIGN_PARAGRAPH.CENTER
            cell.paragraphs[0].runs[0].font.size = Pt(11)

    doc.add_page_break()

    # -------------------------------------------------------------
    # KATA PENGANTAR
    # -------------------------------------------------------------
    p_kp_title = doc.add_paragraph()
    p_kp_title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r = p_kp_title.add_run("KATA PENGANTAR")
    r.font.size = Pt(14)
    r.bold = True
    p_kp_title.paragraph_format.space_after = Pt(16)

    paragraphs_kp = [
        "Puji dan syukur penulis panjatkan ke hadirat Tuhan Yang Maha Esa atas berkat, rahmat, dan bimbingan-Nya sehingga Laporan Pertanggungjawaban (LPJ) Tugas Akhir Praktik Pengembangan Aplikasi Mobile ini dapat terselesaikan dengan baik dan tepat waktu.",
        "Laporan ini disusun sebagai bentuk dokumentasi teknis, pertanggungjawaban karya, dan pembuktian capaian kompetensi kejuruan dalam mata pelajaran Pemrograman Perangkat Bergerak di jurusan Sistem Informasi, Jaringan, dan Aplikasi (SIJA) SMK Negeri 2 Yogyakarta. Proyek yang dikembangkan adalah aplikasi mobile e-commerce fesyen dan olahraga bertajuk \"ShopSey\".",
        "Aplikasi ShopSey dirancang dengan mengadopsi standar terkini industri pengembangan Android, memanfaatkan toolkit deklaratif Jetpack Compose, Material Design 3, arsitektur Single-Activity berbasis Navigation Compose, serta simulasi notifikasi sistem lokal Android untuk memvalidasi alur autentikasi OTP secara mandiri tanpa ketergantungan server backend.",
        "Penulis menyampaikan ucapan terima kasih yang tulus dan penghargaan setinggi-tingginya kepada:\n"
        "1. Kepala SMK Negeri 2 Yogyakarta yang memfasilitasi sarana dan prasarana laboratorium komputer.\n"
        "2. Guru Pembimbing Mata Pelajaran Pemrograman Perangkat Bergerak atas arahan, bimbingan, dan evaluasi teknis yang berharga.\n"
        "3. Orang tua dan keluarga tercinta yang senantiasa memberikan doa dan dukungan moral maupun material.\n"
        "4. Rekan-rekan siswa kelas SIJA atas diskusi konstruktif selama proses pembuatan proyek.",
        "Penulis menyadari bahwa proyek ini masih bersifat purwarupa antarmuka fungsional (functional UI prototype) yang berjalan dengan basis Dummy / Mock Data lokal dan belum terhubung ke database cloud Firebase. Kritik dan saran yang membangun senantiasa diharapkan guna penyempurnaan proyek di masa mendatang."
    ]

    for text in paragraphs_kp:
        p = doc.add_paragraph()
        p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
        p.paragraph_format.first_line_indent = Cm(1.0)
        p.add_run(text)
        p.paragraph_format.space_after = Pt(8)

    p_kp_sign = doc.add_paragraph()
    p_kp_sign.alignment = WD_ALIGN_PARAGRAPH.RIGHT
    p_kp_sign.add_run("Yogyakarta, 28 Oktober 2026\n\nPenulis (Rafi Ardiansyah)")
    p_kp_sign.paragraph_format.space_after = Pt(12)

    doc.add_page_break()

    # -------------------------------------------------------------
    # DAFTAR ISI
    # -------------------------------------------------------------
    p_di_title = doc.add_paragraph()
    p_di_title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r = p_di_title.add_run("DAFTAR ISI")
    r.font.size = Pt(14)
    r.bold = True
    p_di_title.paragraph_format.space_after = Pt(16)

    toc_items = [
        ("HALAMAN JUDUL / COVER", "i"),
        ("LEMBAR PENGESAHAN", "ii"),
        ("KATA PENGANTAR", "iii"),
        ("DAFTAR ISI", "iv"),
        ("BAB I: PENDAHULUAN", "1"),
        ("  1.1 Latar Belakang Masalah", "1"),
        ("  1.2 Rumusan Masalah", "2"),
        ("  1.3 Tujuan Proyek", "2"),
        ("  1.4 Batasan Masalah & Ruang Lingkup (Status Dummy Data / Non-Firebase)", "2"),
        ("  1.5 Manfaat Proyek", "3"),
        ("BAB II: LANDASAN TEORI & TEKNOLOGI PENDUKUNG", "4"),
        ("  2.1 Android Native & Bahasa Pemrograman Kotlin", "4"),
        ("  2.2 Deklaratif UI Toolkit: Jetpack Compose & Material 3", "4"),
        ("  2.3 Single-Activity Architecture & Android Navigation Compose", "5"),
        ("  2.4 State Management & Reaktif UI", "5"),
        ("  2.5 Android Notification Manager API", "6"),
        ("  2.6 Konsep Prototyping & In-Memory Dummy Data", "6"),
        ("BAB III: PERANCANGAN SISTEM & DESAIN", "7"),
        ("  3.1 Filosofi Desain (Monochrome Minimalist & Edge-to-Edge)", "7"),
        ("  3.2 Skema Warna & Aspek Rasio Visual", "7"),
        ("  3.3 Diagram Alir Sistem (Application Flowchart)", "8"),
        ("  3.4 Struktur Direktori & Modul Proyek", "9"),
        ("BAB IV: IMPLEMENTASI FITUR & MODUL APLIKASI", "10"),
        ("  4.1 Modul Splash & Alur Autentikasi Interaktif", "10"),
        ("  4.2 Modul Beranda (Home Snap Pager & Floating Navbar)", "11"),
        ("  4.3 Modul Katalog Belanja (Shop Bento Mosaic & Lifestyle Picks)", "12"),
        ("  4.4 Modul Pencarian & Kategori Populer (Search Screen)", "13"),
        ("  4.5 Modul Detail Produk (Air Jordan 4 'Toro' Multi-Angle Gallery)", "14"),
        ("  4.6 Modul Keranjang Belanja & Checkout (Bag Screen)", "15"),
        ("  4.7 Modul Profil Pengguna & Keanggotaan (Profile Screen)", "16"),
        ("BAB V: PENGUJIAN & EVALUASI SISTEM", "17"),
        ("  5.1 Metode Pengujian (Blackbox Testing)", "17"),
        ("  5.2 Tabel Hasil Pengujian Fungsionalitas Aplikasi", "17"),
        ("  5.3 Evaluasi Kendala Tampilan (Negative Space / Color Tint) dan Solusinya", "19"),
        ("BAB VI: RENCANA PENGEMBANGAN SISTEM (ROADMAP)", "20"),
        ("  6.1 Rencana Migrasi ke Cloud Database (Firebase Firestore)", "20"),
        ("  6.2 Rencana Integrasi Autentikasi Nyata (Firebase Auth)", "20"),
        ("  6.3 Rencana Persistensi Lokal (Room Database) & Payment Gateway", "21"),
        ("BAB VII: KESIMPULAN & SARAN", "22"),
        ("  7.1 Kesimpulan", "22"),
        ("  7.2 Saran", "22"),
        ("DAFTAR PUSTAKA", "23"),
        ("LAMPIRAN: CUPLIKAN KODE KUNCI & TANGKAPAN LAYAR", "24")
    ]

    for item, page in toc_items:
        p = doc.add_paragraph()
        p.paragraph_format.space_after = Pt(3)
        p.paragraph_format.line_spacing = 1.15
        r_item = p.add_run(item)
        r_item.font.size = Pt(11)
        if item.startswith("BAB") or item in ["HALAMAN JUDUL / COVER", "LEMBAR PENGESAHAN", "KATA PENGANTAR", "DAFTAR ISI", "DAFTAR PUSTAKA", "LAMPIRAN: CUPLIKAN KODE KUNCI & TANGKAPAN LAYAR"]:
            r_item.bold = True
        # Tab leader
        r_dots = p.add_run(f"  .............................................................................................  ")
        r_dots.font.color.rgb = RGBColor(0xAA, 0xAA, 0xAA)
        r_dots.font.size = Pt(9)
        r_page = p.add_run(page)
        r_page.font.size = Pt(11)
        r_page.bold = True

    doc.add_page_break()

    # Helper for adding sections
    def add_chapter_heading(chap_num, chap_title):
        p = doc.add_paragraph()
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        r1 = p.add_run(f"{chap_num}\n")
        r1.font.size = Pt(14)
        r1.bold = True
        r2 = p.add_run(chap_title)
        r2.font.size = Pt(14)
        r2.bold = True
        p.paragraph_format.space_before = Pt(6)
        p.paragraph_format.space_after = Pt(16)

    def add_section_heading(sec_title):
        p = doc.add_paragraph()
        r = p.add_run(sec_title)
        r.font.size = Pt(12)
        r.bold = True
        r.font.color.rgb = RGBColor(0x11, 0x11, 0x11)
        p.paragraph_format.space_before = Pt(10)
        p.paragraph_format.space_after = Pt(4)

    def add_body_paragraph(text):
        p = doc.add_paragraph()
        p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
        p.paragraph_format.first_line_indent = Cm(1.0)
        p.paragraph_format.space_after = Pt(6)
        p.add_run(text)
        return p

    def add_callout_box(text, prefix="CATATAN PENTING: "):
        table = doc.add_table(rows=1, cols=1)
        table.alignment = WD_TABLE_ALIGNMENT.CENTER
        cell = table.cell(0, 0)
        cell.width = Cm(15.0)
        set_cell_background(cell, "F2F4F7")
        set_cell_margins(cell, top=140, bottom=140, left=200, right=200)

        p = cell.paragraphs[0]
        p.paragraph_format.space_after = Pt(0)
        r_pre = p.add_run(prefix)
        r_pre.bold = True
        r_pre.font.color.rgb = RGBColor(0x00, 0x33, 0x66)
        r_txt = p.add_run(text)
        r_txt.font.italic = True
        doc.add_paragraph().paragraph_format.space_after = Pt(4)

    # -------------------------------------------------------------
    # BAB I: PENDAHULUAN
    # -------------------------------------------------------------
    add_chapter_heading("BAB I", "PENDAHULUAN")

    add_section_heading("1.1 Latar Belakang Masalah")
    add_body_paragraph(
        "Perkembangan teknologi komputasi bergerak (mobile computing) dalam dekade terakhir telah mengubah lanskap perdagangan ritel secara fundamental. Sektor e-commerce fesyen dan pakaian olahraga (sportswear & lifestyle gear) menjadi salah satu industri dengan pertumbuhan paling pesat. Aplikasi digital berskala global seperti Nike, Jordan, Adidas, dan GOAT telah membuktikan bahwa keunggulan visual, kecepatan respons, dan kesederhanaan antarmuka yang elegan memegang peranan krusial dalam membentuk persepsi merek serta loyalitas pengguna."
    )
    add_body_paragraph(
        "Dalam proses perancangan perangkat lunak Android, industri telah bertransformasi dari pendekatan berbasis XML Layout dan Fragment Manager menuju paradigma modern berbasis Declarative UI, yaitu Jetpack Compose yang didukung penuh oleh bahasa pemrograman Kotlin. Pendekatan deklaratif menawarkan produktivitas tinggi, pemisahan dependensi kode yang bersih, serta kemampuan membangun animasi dan interaksi tingkat lanjut secara langsung melalui fungsi komposabel (@Composable)."
    )
    add_body_paragraph(
        "Berdasarkan kompetensi kejuruan dalam kurikulum Sistem Informasi, Jaringan, dan Aplikasi (SIJA), siswa dituntut untuk mampu merancang, membangun, menguji, dan mendokumentasikan aplikasi mobile yang memenuhi standar industri. Oleh karena itu, dirancang proyek aplikasi \"ShopSey\", sebuah aplikasi e-commerce fesyen dan sepatu olahraga dengan nuansa monokromatik premium (hitam-putih), yang mengimplementasikan arsitektur navigasi Single-Activity, alur autentikasi interaktif, serta penanganan rasio tampilan gambar secara presisi tanpa celah."
    )

    add_section_heading("1.2 Rumusan Masalah")
    add_body_paragraph(
        "Rumusan masalah yang menjadi fokus penyusunan laporan dan pengembangan aplikasi ini adalah:\n"
        "1. Bagaimana merancang dan mengimplementasikan antarmuka aplikasi e-commerce fesyen modern menggunakan Android Jetpack Compose dan Material Design 3?\n"
        "2. Bagaimana membangun arsitektur navigasi Single-Activity berbasis Navigation Compose yang mampu menangani alur autentikasi lengkap serta navigasi halaman utama secara mulus?\n"
        "3. Bagaimana menyelesaikan permasalahan tata letak visual, khususnya ruang kosong (white space / negative space) dan ketidaksesuaian warna latar belakang wadah pada galeri produk detail dan grid kartu belanja agar tampil penuh (edge-to-edge)?"
    )

    add_section_heading("1.3 Tujuan Proyek")
    add_body_paragraph(
        "Tujuan yang hendak dicapai melalui pelaksanaan proyek tugas praktik ini meliputi:\n"
        "1. Membangun purwarupa fungsional aplikasi mobile Android bertema e-commerce fesyen menggunakan bahasa Kotlin dan Jetpack Compose.\n"
        "2. Menerapkan manajemen state lokal (Compose State) dan perutean dinamis antar halaman (NavHost, NavController, dan NavBackStackEntry).\n"
        "3. Mengimplementasikan fitur simulasi autentikasi cerdas menggunakan notifikasi sistem Android lokal (Heads-up Notification) untuk verifikasi kode OTP.\n"
        "4. Melakukan optimasi estetika antarmuka dengan menghilangkan margin yang tidak diinginkan dan menerapkan ContentScale.Crop pada wadah gambar beresolusi tinggi."
    )

    add_section_heading("1.4 Batasan Masalah & Ruang Lingkup (Status Dummy Data / Non-Firebase)")
    add_body_paragraph(
        "Untuk menjaga fokus dan ruang lingkup tugas praktik akhir, ditetapkan beberapa batasan masalah teknis sebagai berikut:"
    )
    add_callout_box(
        "Proyek aplikasi ShopSey pada tahap ini BELUM MENGGUNAKAN DATABASE BACKEND CLOUD (seperti Google Firebase Realtime Database, Cloud Firestore, maupun REST API eksternal). Seluruh data katalog produk, kategori, data sesi pengguna, dan status keranjang dikelola sepenuhnya menggunakan Dummy / Mock Data lokal (In-Memory State). Simulasi verifikasi OTP dibangkitkan melalui NotificationManager sistem Android secara offline.",
        prefix="STATUS DATABASE & BACKEND: "
    )
    add_body_paragraph(
        "1. Aplikasi beroperasi secara mandiri (standalone/offline) tanpa memerlukan koneksi ke server backend eksternal.\n"
        "2. Fungsionalitas autentikasi, pengaturan ukuran sepatu, ulasan produk, dan checkout keranjang dirancang sebagai simulasi antarmuka pengguna (UI/UX Prototyping).\n"
        "3. Target pengujian adalah perangkat dengan sistem operasi Android 8.0 (API Level 26) ke atas hingga Android 14/15."
    )

    add_section_heading("1.5 Manfaat Proyek")
    add_body_paragraph(
        "Manfaat yang diperoleh dari pelaksanaan proyek tugas akhir praktik ini adalah:\n"
        "• Bagi Siswa: Meningkatkan penguasaan praktis terhadap teknologi Jetpack Compose, arsitektur kode modern Android, manajemen aset resolusi tinggi, dan penyusunan laporan teknis terstandar.\n"
        "• Bagi Institusi Pendidikan: Menjadi artefak hasil belajar dan portofolio kejuruan SIJA yang merepresentasikan kesiapan lulusan terhadap perkembangan ekosistem industri software mobile modern."
    )

    doc.add_page_break()

    # -------------------------------------------------------------
    # BAB II: LANDASAN TEORI & TEKNOLOGI
    # -------------------------------------------------------------
    add_chapter_heading("BAB II", "LANDASAN TEORI & TEKNOLOGI PENDUKUNG")

    add_section_heading("2.1 Android Native & Bahasa Pemrograman Kotlin")
    add_body_paragraph(
        "Pengembangan aplikasi native pada sistem operasi Android memberikan akses penuh dan performa optimal terhadap perangkat keras dan pustaka sistem operasi. Google menetapkan Kotlin sebagai bahasa utama pengembangan Android resmi sejak Google I/O 2019. Kotlin memiliki keunggulan berupa keamanan tipe (null safety), ekspresi fungsi tingkat tinggi (higher-order functions), sintaksis yang ringkas, serta integrasi mulus dengan pustaka Java eksisting."
    )

    add_section_heading("2.2 Deklaratif UI Toolkit: Jetpack Compose & Material 3")
    add_body_paragraph(
        "Jetpack Compose merevolusi perancangan UI Android dengan meninggalkan sistem layout XML imperatif. Melalui Compose, antarmuka didefinisikan sebagai fungsi yang memetakan data menjadi elemen UI. Ketika data berubah, Compose secara otomatis menjalankan rekomposisi (recomposition) hanya pada komponen yang terpengaruh, sehingga meningkatkan efisiensi komputasi visual. ShopSey menggunakan komponen Material Design 3 (MaterialTheme, Surface, Card, HorizontalPager, VerticalPager) yang menjamin konsistensi kontras, elevasi, dan radius sudut (shape rounding)."
    )

    add_section_heading("2.3 Single-Activity Architecture & Android Navigation Compose")
    add_body_paragraph(
        "Arsitektur Single-Activity merekomendasikan penggunaan satu Activity sebagai kontainer utama, di mana transisi layar sepenuhnya dikelola oleh komponen navigasi tingkat lanjut. Melalui pustaka androidx.navigation:navigation-compose, perpindahan rute (route), pengiriman argumen URL (seperti email ter-encode), dan manipulasi tumpukan kembali (back-stack) dikontrol secara deklaratif dalam satu NavHost terpusat."
    )

    add_section_heading("2.4 State Management & Reaktif UI")
    add_body_paragraph(
        "Manajemen state dalam Jetpack Compose menjadi inti responsivitas aplikasi. API kunci yang digunakan dalam proyek ini meliputi:\n"
        "• remember { mutableStateOf(value) }: Menyimpan nilai state yang bertahan selama siklus hidup rekomposisi, misalnya input teks email, status ekspansi menu accordion, dan ukuran sepatu terpilih.\n"
        "• rememberPagerState(): Mengatur posisi index halaman saat ini pada komponen carousel geser (HorizontalPager).\n"
        "• rememberScrollState(): Mengendalikan dan mengingat posisi pengguliran vertikal pada konten yang melebihi tinggi layar."
    )

    add_section_heading("2.5 Android Notification Manager API")
    add_body_paragraph(
        "Pemberitahuan sistem pada Android diatur melalui layanan NotificationManager. Sejak Android 8.0 (API 26), setiap notifikasi wajib dikaitkan dengan NotificationChannel. Melalui NotificationCompat.Builder, aplikasi dapat menampilkan notifikasi prioritas tinggi (PRIORITY_HIGH) yang memicu pop-up (heads-up banner) langsung di atas layar pengguna."
    )

    add_section_heading("2.6 Konsep Prototyping & In-Memory Dummy Data")
    add_body_paragraph(
        "Dalam metodologi rekayasa perangkat lunak, perancangan purwarupa fungsional (functional prototype) sering menggunakan Dummy Data / Mocking Data sebelum integrasi database nyata dilakukan. Pendekatan ini memungkinkan pengembang dan penguji untuk memvalidasi alur interaksi, ergonomi tata letak, dan logika antarmuka tanpa terbebani oleh latensi jaringan, kuota API cloud, atau dependensi server backend."
    )

    doc.add_page_break()

    # -------------------------------------------------------------
    # BAB III: PERANCANGAN SISTEM & DESAIN
    # -------------------------------------------------------------
    add_chapter_heading("BAB III", "PERANCANGAN SISTEM & DESAIN")

    add_section_heading("3.1 Filosofi Desain (Monochrome Minimalist & Edge-to-Edge)")
    add_body_paragraph(
        "Sesuai dokumen DESIGN.md, filosofi estetika ShopSey mengacu pada gaya Minimalis Monokromatik yang terinspirasi dari aplikasi ritel kelas dunia. Desain ini bertujuan memberikan kesan bersih, mewah, dan berfokus penuh pada keindahan detail produk olahraga:"
    )
    add_body_paragraph(
        "• Warna Primer (Primary): Hitam Pekat (#111111) digunakan untuk tombol tindakan utama (Call to Action), teks judul tebal, ikon navigasi aktif, dan status indikator.\n"
        "• Warna Latar (Background): Putih Murni (#FFFFFF) sebagai kanvas utama untuk menghadirkan kontras tinggi dan pencahayaan bersih.\n"
        "• Warna Aksen (Neutral Accent): Abu-abu lembut (#F4F4F6, #EEEEF0) digunakan untuk garis pemisah tipis dan latar kontainer kartu.\n"
        "• Prinsip Edge-to-Edge: Gambar produk utama wajib mengisi penuh lebar perangkat tanpa margin samping yang menimbulkan batas warna tidak serasi."
    )

    add_section_heading("3.2 Diagram Alir Navigasi Aplikasi")
    add_body_paragraph(
        "Aplikasi ShopSey dirancang dengan alur pengguna yang runut, dimulai dari pembukaan aplikasi, autentikasi terpadu, hingga penjelajahan halaman belanja utama:"
    )

    flow_table = doc.add_table(rows=6, cols=3)
    flow_table.alignment = WD_TABLE_ALIGNMENT.CENTER
    set_table_borders(flow_table)

    headers = ["Tahap", "Rute / Komponen", "Deskripsi Perilaku Antarmuka"]
    for i, h in enumerate(headers):
        cell = flow_table.rows[0].cells[i]
        cell.paragraphs[0].text = h
        cell.paragraphs[0].runs[0].bold = True
        set_cell_background(cell, "EAEAEA")

    flow_data = [
        ("1. Pembukaan", "splash -> landing_screen", "SplashScreen menampilkan logo minimalis, beralih ke LandingScreen (pilihan Sign In / Sign Up)."),
        ("2. Autentikasi", "email_input -> otp / password", "Pengguna memasukkan email. Alur Sign In memicu notifikasi sistem lokal berisi kode OTP 6-digit."),
        ("3. Onboarding", "login_success -> welcome_screen", "Konfirmasi login sukses diikuti tampilan visual 3D kolase gambar sambutan anggota."),
        ("4. Beranda", "home (Tab 0)", "Menampilkan Snap Pager vertikal sinematik layar penuh (Nike, Jordan, Kobe) & Floating Navbar."),
        ("5. Belanja & Detail", "shop (Tab 1) -> product_detail", "Katalog Mozaik Bento & Grid 3-kolom. Klik item membuka galeri multi-sudut edge-to-edge."),
    ]

    for row_idx, row_content in enumerate(flow_data, start=1):
        for col_idx, col_text in enumerate(row_content):
            cell = flow_table.rows[row_idx].cells[col_idx]
            cell.paragraphs[0].text = col_text
            cell.paragraphs[0].runs[0].font.size = Pt(10)

    doc.add_paragraph().paragraph_format.space_after = Pt(8)

    add_section_heading("3.3 Struktur Direktori Proyek")
    add_body_paragraph(
        "Arsitektur kode diatur berdasarkan fitur fungsional (feature-based packaging) di dalam paket com.example.shopsey untuk mempermudah pemeliharaan:"
    )

    code_dir_table = doc.add_table(rows=1, cols=1)
    code_dir_table.alignment = WD_TABLE_ALIGNMENT.CENTER
    c_cell = code_dir_table.cell(0, 0)
    c_cell.width = Cm(15.0)
    set_cell_background(c_cell, "F8F8FA")
    set_cell_margins(c_cell, top=100, bottom=100, left=150, right=150)
    p_code = c_cell.paragraphs[0]
    p_code.paragraph_format.line_spacing = 1.15
    p_code.paragraph_format.space_after = Pt(0)
    r_code = p_code.add_run(
        "app/src/main/java/com/example/shopsey/\n"
        "├── MainActivity.kt        # NavHost tunggal pengatur seluruh rute aplikasi\n"
        "└── ui/\n"
        "    ├── splash/            # SplashScreen.kt (Animasi pembuka logo ShopSey)\n"
        "    ├── auth/              # Modul Autentikasi Lengkap (9 Komponen Layar)\n"
        "    ├── home/              # HomeScreen.kt (Snap Pager Sinematik & Floating Nav)\n"
        "    ├── shop/              # ShopScreen.kt (Banner Carousel, Bento Card, Lifestyle Grid)\n"
        "    ├── search/            # SearchScreen.kt (Live Filter & Ubin Kategori Populer)\n"
        "    ├── detail/            # ProductDetailScreen.kt (Galeri 8 Foto Edge-to-Edge & Ulasan)\n"
        "    ├── bag/               # BagScreen.kt (Keranjang Belanja Air Jordan 4 Toro)\n"
        "    └── profile/           # ProfileScreen.kt (Profil Anggota & Pengaturan Akun)"
    )
    r_code.font.name = "Courier New"
    r_code.font.size = Pt(9.5)
    r_code.font.color.rgb = RGBColor(0x22, 0x22, 0x22)
    doc.add_paragraph().paragraph_format.space_after = Pt(6)

    doc.add_page_break()

    # -------------------------------------------------------------
    # BAB IV: IMPLEMENTASI FITUR & MODUL APLIKASI
    # -------------------------------------------------------------
    add_chapter_heading("BAB IV", "IMPLEMENTASI FITUR & MODUL APLIKASI")

    add_section_heading("4.1 Modul Splash & Alur Autentikasi Interaktif")
    add_body_paragraph(
        "Modul autentikasi ShopSey dibangun secara mendalam dan menyeluruh untuk menyajikan pengalaman pengguna yang menyerupai aplikasi ritel komersial:"
    )
    add_body_paragraph(
        "1. SplashScreen: Menampilkan ikon logo ShopSey di tengah latar hitam dengan animasi pemudaran (fade transition) yang otomatis berpindah ke LandingScreen.\n"
        "2. LandingScreen: Memberikan opsi pilihan awal yang jelas antara \"Sign In\" (Masuk) atau \"Sign Up\" (Daftar Akun Baru).\n"
        "3. EmailInputScreen: Menerima input alamat email dengan validasi format teks dan meneruskan parameter email ke rute berikutnya.\n"
        "4. OtpVerificationScreen & Simulasi Notifikasi: Pada halaman ini, tombol verifikasi memicu fungsi lokal Android NotificationManager yang mengirimkan Heads-up Notification berisi kode 6-digit. Pengguna dapat langsung melihat kode tersebut pada bilah notifikasi sistem Android tanpa perlu server SMS/Email eksternal.\n"
        "5. PasswordInputScreen & ForgotPasswordScreen: Menyediakan formulir kata sandi dan alur pemulihan akun jika pengguna lupa kata sandi.\n"
        "6. RegisterFormScreen & WelcomeScreen: Formulir registrasi identitas diri (nama depan, nama belakang, tanggal lahir) yang diakhiri dengan tampilan visual 3D kolase foto miring pada WelcomeScreen."
    )

    add_section_heading("4.2 Modul Beranda (Home Snap Pager & Floating Navbar)")
    add_body_paragraph(
        "Halaman utama (HomeScreen.kt) mengimplementasikan VerticalPager dengan efek snapping. Setiap halaman penuh menampilkan kampanye merek terkemuka (Nike, Sheath Kobe, Air Jordan) dengan gradien gelap sinematik pada bagian bawah agar teks judul dan tombol aksi tetap terbaca dengan jelas. Selain itu, terdapat bilah navigasi melayang (Floating Dynamic Bottom Navigation) yang memiliki 5 tab ikonik: Home, Shop, Search, Bag, dan Profile."
    )

    add_section_heading("4.3 Modul Katalog Belanja (Shop Bento Mosaic & Lifestyle Picks)")
    add_body_paragraph(
        "Halaman belanja (ShopScreen.kt) disusun dengan tata letak mozaik bertingkat:\n"
        "• Hero Banner Pager: Banner promosi otomatis dengan indikator pil dinamis.\n"
        "• Kategori Mozaik (2 Kolom): Kartu bento kategori busana dan olahraga.\n"
        "• Jump Back In: Daftar riwayat produk yang baru dilihat dengan rasio bujur sangkar 130dp.\n"
        "• Lifestyle Picks Grid 3-Kolom: Grid produk berisi 9 item fesyen dan sepatu. Setiap wadah kartu menggunakan aspectRatio(1f) dan ContentScale.Crop sehingga gambar produk terpotong proporsional dan membungkus kartu tanpa celah ruang abu-abu."
    )

    add_section_heading("4.4 Modul Pencarian & Kategori Populer (Search Screen)")
    add_body_paragraph(
        "Modul pencarian (SearchScreen.kt) menyediakan kotak pencarian interaktif yang secara instan menyaring daftar produk tiruan. Di bawah bilah pencarian, terdapat ubin kategori populer (Running, Basketball, Lifestyle, Gym) dengan lapisan gradien gelap dan judul kontras tinggi, diikuti hasil pencarian berformat kartu kompak 72dp."
    )

    add_section_heading("4.5 Modul Detail Produk (Air Jordan 4 'Toro' Multi-Angle Gallery)")
    add_body_paragraph(
        "Halaman detail produk (ProductDetailScreen.kt) merupakan implementasi kunci yang mereplika halaman resmi sepatu Air Jordan 4 'Toro':\n"
        "• Galeri Multi-Sudut Edge-to-Edge: Menggunakan HorizontalPager dengan 8 gambar resolusi tinggi (detail1.png s/d detail8.png berukuran 1070x1338 piksel). Lebar kontainer disetel Modifier.fillMaxWidth() dengan aspectRatio(1070f / 1338f) dan ContentScale.Crop. Seluruh foto membentang dari tepi kiri layar hingga tepi kanan layar tanpa ada white space atau ketidaksesuaian warna latar belakang.\n"
        "• Indikator Titik Pager: Titik pelacak slide aktif dengan transisi ukuran dinamis.\n"
        "• Pemilihan Ukuran Sepatu (Size Selector): Tombol ukuran mulai dari EU 40 hingga EU 47.5 dengan status ketersediaan interaktif.\n"
        "• Accordion Ulasan & Rekomendasi: Komponen ulasan berbintang yang dapat diperluas serta deretan kartu rekomendasi produk di bagian bawah."
    )

    add_section_heading("4.6 Modul Keranjang Belanja & Checkout (Bag Screen)")
    add_body_paragraph(
        "Halaman keranjang (BagScreen.kt) menampilkan item pesanan sepatu Air Jordan 4 'Toro' dengan kuantitas yang dapat disesuaikan, rincian pengiriman gratis, estimasi total belanja, dan tombol Checkout hitam solid."
    )

    add_section_heading("4.7 Modul Profil Pengguna & Keanggotaan (Profile Screen)")
    add_body_paragraph(
        "Halaman profil (ProfileScreen.kt) memuat informasi pengguna dengan avatar inisial, asal kota Yogyakarta, 4 baris aksi cepat (Pesanan, Akses, Favorit, Pengaturan), serta menu Manfaat Anggota ShopSey dan daftar atlet yang diikuti."
    )

    doc.add_page_break()

    # -------------------------------------------------------------
    # BAB V: PENGUJIAN & EVALUASI SISTEM
    # -------------------------------------------------------------
    add_chapter_heading("BAB V", "PENGUJIAN & EVALUASI SISTEM")

    add_section_heading("5.1 Metode Pengujian (Blackbox Testing)")
    add_body_paragraph(
        "Pengujian perangkat lunak dilakukan dengan metode Blackbox Testing, yaitu mengevaluasi keluaran antarmuka dan fungsi sistem berdasarkan skenario masukan tanpa memeriksa struktur internal logika mesin secara langsung. Pengujian dilakukan pada emulator Android Studio (Pixel 7 Pro - Android 14 API 34) dan perangkat fisik Android."
    )

    add_section_heading("5.2 Tabel Hasil Pengujian Fungsionalitas")

    test_table = doc.add_table(rows=9, cols=4)
    test_table.alignment = WD_TABLE_ALIGNMENT.CENTER
    set_table_borders(test_table)

    test_headers = ["No", "Fitur yang Diuji", "Skenario Uji & Masukan", "Hasil Pengamatan & Status"]
    for i, th in enumerate(test_headers):
        c = test_table.rows[0].cells[i]
        c.paragraphs[0].text = th
        c.paragraphs[0].runs[0].bold = True
        set_cell_background(c, "EAEAEA")

    test_rows = [
        ("1", "Splash Screen Transition", "Aplikasi diluncurkan dari launcher perangkat.", "Logo ShopSey tampil di tengah, setelah 1.8 detik otomatis berpindah ke LandingScreen. [BERHASIL]"),
        ("2", "Navigasi Sign In & Input Email", "Pengguna menekan tombol Sign In dan memasukkan email valid.", "Rute berpindah ke halaman input OTP dan email ter-encode dengan aman. [BERHASIL]"),
        ("3", "Simulasi Notifikasi Sistem OTP", "Menekan tombol verifikasi email pada OtpVerificationScreen.", "Notifikasi pop-up heads-up Android muncul menampilkan kode 6-digit. [BERHASIL]"),
        ("4", "Floating Bottom Navigation", "Menekan bergantian tab Home, Shop, Search, Bag, dan Profile.", "Konten layar berganti secara instan dengan transisi visual yang halus. [BERHASIL]"),
        ("5", "Galeri Detail Produk Edge-to-Edge", "Membuka halaman produk Air Jordan 4 'Toro'.", "Kedelapan foto produk membentang penuh selebar layar tanpa celah samping. [BERHASIL]"),
        ("6", "Size Picker Selection", "Memilih salah satu ukuran sepatu (misal: EU 42).", "Tombol terarsir hitam solid dan state ukuran terpilih tersimpan aktif. [BERHASIL]"),
        ("7", "Grid Produk Halaman Shop", "Menggulir ke bagian Lifestyle Picks pada tab Shop.", "9 gambar produk tertata dalam grid 3-kolom rapi berasio bujur sangkar 1:1. [BERHASIL]"),
        ("8", "Item Keranjang & Checkout", "Membuka tab Bag dan menekan tombol Checkout.", "Foto produk Toro tampil bersih tanpa padding abu-abu, kalkulasi harga tepat. [BERHASIL]")
    ]

    for r_idx, r_data in enumerate(test_rows, start=1):
        for c_idx, c_val in enumerate(r_data):
            cell = test_table.rows[r_idx].cells[c_idx]
            cell.paragraphs[0].text = c_val
            cell.paragraphs[0].runs[0].font.size = Pt(9.5)

    doc.add_paragraph().paragraph_format.space_after = Pt(8)

    add_section_heading("5.3 Evaluasi Kendala Tampilan (Negative Space / Color Tint) dan Solusinya")
    add_body_paragraph(
        "Dalam proses pengujian antarmuka, ditemukan sebuah kendala visual signifikan yang langsung dievaluasi dan diselesaikan:"
    )
    add_body_paragraph(
        "• Deskripsi Masalah: Pada versi rancangan awal, galeri foto produk pada ProductDetailScreen menggunakan Box dengan aspectRatio(1.08f) dan padding(horizontal = 24.dp, vertical = 18.dp) serta background(Color(0xFFF7F7F8)). Karena foto asli memiliki rasio tegak (1070x1338 piksel, ~0.8), gambar menyusut dan menyisakan ruang kosong (white space / negative space) lebar di sisi kiri dan kanan. Hal ini menyebabkan perbedaan warna yang kontras antara warna abu-abu wadah dengan latar belakang studio foto produk."
    )
    add_body_paragraph(
        "• Solusi Rekayasa yang Diterapkan: Kontainer diubah menggunakan rasio asli 1070f / 1338f, menghilangkan seluruh padding horizontal/vertikal, menghapus warna latar belakang wadah pembungkus, serta menetapkan ContentScale.Crop pada komponen Image. Hal serupa diterapkan pada ProductGridItem di halaman Shop (menghilangkan padding 8dp dan menggunakan aspectRatio(1f)). Hasilnya, seluruh foto produk tampil membentang sempurna selebar layar pengguna (edge-to-edge) dengan estetika yang sangat memuaskan."
    )

    doc.add_page_break()

    # -------------------------------------------------------------
    # BAB VI: RENCANA PENGEMBANGAN SISTEM (ROADMAP)
    # -------------------------------------------------------------
    add_chapter_heading("BAB VI", "RENCANA PENGEMBANGAN SISTEM (ROADMAP)")

    add_body_paragraph(
        "Mengingat saat ini proyek ShopSey berfokus pada keunggulan antarmuka pengguna (UI/UX) dan simulasi data lokal (dummy), telah dirancang peta jalan pengembangan sistem (development roadmap) untuk mentransformasikan aplikasi ini menjadi platform e-commerce komersial berskala penuh:"
    )

    add_section_heading("6.1 Rencana Migrasi ke Cloud Database (Firebase Firestore / Realtime DB)")
    add_body_paragraph(
        "Langkah pertama adalah mengganti koleksi data tiruan in-memory dengan Google Cloud Firestore. Setiap produk, varian ukuran, harga diskon, dan ketersediaan stok akan disimpan dalam format dokumen NoSQL yang dapat disinkronisasikan secara real-time ke seluruh perangkat pengguna saat terjadi pembaruan data dari panel admin."
    )

    add_section_heading("6.2 Rencana Integrasi Autentikasi Nyata (Firebase Authentication)")
    add_body_paragraph(
        "Fitur verifikasi OTP yang saat ini disimulasikan menggunakan NotificationManager lokal akan ditingkatkan dengan mengintegrasikan Firebase Phone Authentication (layanan SMS Gateway resmi Google) atau verifikasi email tautan langsung (Email Link Auth) serta fitur Single Sign-On (Google Sign-In) untuk meningkatkan keamanan akun pengguna."
    )

    add_section_heading("6.3 Rencana Persistensi Lokal (Room Database) & Payment Gateway")
    add_body_paragraph(
        "• Penyimpanan Lokal Persisten: Menerapkan pustaka Jetpack Room Database (berbasis SQLite) untuk menyimpan isi keranjang belanja (Bag) dan daftar keinginan (Wishlist) secara persisten di media penyimpanan perangkat, sehingga data tidak terhapus saat aplikasi dimatikan.\n"
        "• Integrasi Gerbang Pembayaran: Menghubungkan alur Checkout dengan API Payment Gateway pihak ketiga (seperti Midtrans atau Xendit) guna mendukung transaksi pembayaran nyata via QRIS, GoPay, OVO, ShopeePay, dan Transfer Virtual Account Bank."
    )

    doc.add_page_break()

    # -------------------------------------------------------------
    # BAB VII: KESIMPULAN & SARAN
    # -------------------------------------------------------------
    add_chapter_heading("BAB VII", "KESIMPULAN & SARAN")

    add_section_heading("7.1 Kesimpulan")
    add_body_paragraph(
        "Berdasarkan seluruh tahapan perancangan, implementasi kode, dan pengujian yang telah dilaksanakan pada proyek aplikasi \"ShopSey\", dapat ditarik beberapa kesimpulan sebagai berikut:\n"
        "1. Aplikasi e-commerce fesyen dan olahraga ShopSey telah berhasil dibangun secara native menggunakan bahasa pemrograman Kotlin dan toolkit modern Jetpack Compose Material 3.\n"
        "2. Arsitektur Single-Activity yang dikombinasikan dengan Navigation Compose terbukti sangat andal dalam mengelola tumpukan navigasi yang kompleks (Splash, alur Autentikasi 9-layar, Beranda Snap Pager, Katalog Bento, hingga Detail Produk).\n"
        "3. Simulasi notifikasi sistem Android lokal (Heads-up Notification) berhasil memvalidasi alur verifikasi OTP secara interaktif tanpa memerlukan dependensi server backend eksternal.\n"
        "4. Permasalahan tampilan berupa negative space dan ketidakcocokan warna latar belakang pada galeri produk dan kartu belanja berhasil diatasi melalui penerapan prinsip edge-to-edge layout dan ContentScale.Crop."
    )

    add_section_heading("7.2 Saran")
    add_body_paragraph(
        "Adapun saran-saran yang dapat direkomendasikan untuk pengembangan aplikasi ShopSey pada tahap berikutnya adalah:\n"
        "1. Melanjutkan integrasi backend ke Google Firebase (Cloud Firestore & Firebase Auth) guna mendukung manajemen data terpusat dan inventaris stok multi-pengguna.\n"
        "2. Menambahkan fitur ulasan riil dengan pengunggahan foto dari pengguna serta fitur pencarian cerdas berbasis kamera (Visual Product Search)."
    )

    doc.add_page_break()

    # -------------------------------------------------------------
    # DAFTAR PUSTAKA
    # -------------------------------------------------------------
    add_chapter_heading("DAFTAR PUSTAKA", "")

    daftar_pustaka = [
        "Android Developers. (2024). Jetpack Compose Tutorial and Documentation. Google Developers. Diakses dari https://developer.android.com/jetpack/compose",
        "Android Developers. (2024). Navigation in Jetpack Compose: Single-Activity Architecture. Google Developers. Diakses dari https://developer.android.com/jetpack/compose/navigation",
        "Google. (2024). Material Design 3 Guidelines: Color, Elevation, and Typography. Material.io. Diakses dari https://m3.material.io",
        "JetBrains. (2024). Kotlin Programming Language Documentation: Idiomatic Kotlin and Coroutines. JetBrains s.r.o. Diakses dari https://kotlinlang.org/docs/home.html",
        "Pressman, R. S., & Maxim, B. R. (2020). Software Engineering: A Practitioner's Approach (9th ed.). McGraw-Hill Education.",
        "Repositori Resmi ShopSey. (2026). ShopSey - Elegant E-Commerce Mobile App for Sports and Lifestyle Gear. GitHub. Diakses dari https://github.com/r4rdi/ShopSey-App.git"
    ]

    for dp in daftar_pustaka:
        p = doc.add_paragraph()
        p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
        p.paragraph_format.left_indent = Cm(1.0)
        p.paragraph_format.first_line_indent = Cm(-1.0)
        p.paragraph_format.space_after = Pt(8)
        p.add_run(dp)

    doc.add_page_break()

    # -------------------------------------------------------------
    # LAMPIRAN
    # -------------------------------------------------------------
    add_chapter_heading("LAMPIRAN", "CUPLIKAN KODE KUNCI & TANGKAPAN LAYAR")

    add_section_heading("Lampiran 1: Pengaturan Edge-to-Edge Galeri Produk (ProductDetailScreen.kt)")
    c_table1 = doc.add_table(rows=1, cols=1)
    c_table1.alignment = WD_TABLE_ALIGNMENT.CENTER
    c1 = c_table1.cell(0, 0)
    c1.width = Cm(15.0)
    set_cell_background(c1, "F6F8FA")
    p1 = c1.paragraphs[0]
    p1.paragraph_format.line_spacing = 1.15
    p1.paragraph_format.space_after = Pt(0)
    r1 = p1.add_run(
        "// 2. GALERI FOTO PRODUK (HorizontalPager detail1 s/d detail8)\n"
        "// Kelebaran gambar dipaskan penuh dengan lebar device HP pengguna (edge-to-edge),\n"
        "// tanpa padding atau white space / negative space.\n"
        "Box(\n"
        "    modifier = Modifier\n"
        "        .fillMaxWidth()\n"
        "        .aspectRatio(1070f / 1338f)\n"
        ") {\n"
        "    HorizontalPager(\n"
        "        state = pagerState,\n"
        "        modifier = Modifier.fillMaxSize()\n"
        "    ) { pageIndex ->\n"
        "        val imageRes = product.images.getOrElse(pageIndex) { R.drawable.bag1 }\n"
        "        Image(\n"
        "            painter = painterResource(id = imageRes),\n"
        "            contentDescription = \"${product.title} Angle ${pageIndex + 1}\",\n"
        "            contentScale = ContentScale.Crop,\n"
        "            modifier = Modifier.fillMaxSize()\n"
        "        )\n"
        "    }\n"
        "}"
    )
    r1.font.name = "Courier New"
    r1.font.size = Pt(9)
    doc.add_paragraph().paragraph_format.space_after = Pt(10)

    add_section_heading("Lampiran 2: Simulasi Notifikasi Sistem OTP (OtpVerificationScreen.kt)")
    c_table2 = doc.add_table(rows=1, cols=1)
    c_table2.alignment = WD_TABLE_ALIGNMENT.CENTER
    c2 = c_table2.cell(0, 0)
    c2.width = Cm(15.0)
    set_cell_background(c2, "F6F8FA")
    p2 = c2.paragraphs[0]
    p2.paragraph_format.line_spacing = 1.15
    p2.paragraph_format.space_after = Pt(0)
    r2 = p2.add_run(
        "val notification = NotificationCompat.Builder(context, channelId)\n"
        "    .setSmallIcon(R.drawable.logo3)\n"
        "    .setContentTitle(\"ShopSey\")\n"
        "    .setContentText(\"Kode verifikasi Anda adalah $otpCode. Gunakan untuk masuk.\")\n"
        "    .setStyle(NotificationCompat.BigTextStyle().bigText(\n"
        "        \"Kode verifikasi akun ShopSey Anda adalah $otpCode. Jangan berikan kode ini kepada siapa pun.\"\n"
        "    ))\n"
        "    .setPriority(NotificationCompat.PRIORITY_HIGH)\n"
        "    .setAutoCancel(true)\n"
        "    .build()\n"
        "\n"
        "notificationManager.notify(1001, notification)"
    )
    r2.font.name = "Courier New"
    r2.font.size = Pt(9)
    doc.add_paragraph().paragraph_format.space_after = Pt(12)

    add_section_heading("Lampiran 3: Tangkapan Layar Tampilan Aplikasi (Screenshots)")
    add_body_paragraph("Berikut adalah dokumentasi tampilan antarmuka aplikasi ShopSey yang telah diuji pada perangkat Android:")

    # Add images into document if available
    screenshots = [
        ("app/src/main/res/drawable/splash_screen_page.png", "Gambar L.1: Tampilan Splash Screen & Beranda Pager"),
        ("app/src/main/res/drawable/kategori_page.png", "Gambar L.2: Tampilan Katalog Kategori & Mozaik Belanja (Shop)"),
        ("app/src/main/res/drawable/detail_page.png", "Gambar L.3: Tampilan Halaman Detail Produk Edge-to-Edge Air Jordan 4 'Toro'"),
        ("app/src/main/res/drawable/cart_page.png", "Gambar L.4: Tampilan Keranjang Belanja (Bag Screen) & Ringkasan Checkout")
    ]

    for img_path, caption in screenshots:
        if os.path.exists(img_path):
            try:
                p_img = doc.add_paragraph()
                p_img.alignment = WD_ALIGN_PARAGRAPH.CENTER
                p_img.paragraph_format.space_before = Pt(8)
                p_img.paragraph_format.space_after = Pt(2)
                run_img = p_img.add_run()
                run_img.add_picture(img_path, width=Inches(2.5))

                p_cap = doc.add_paragraph()
                p_cap.alignment = WD_ALIGN_PARAGRAPH.CENTER
                p_cap.paragraph_format.space_after = Pt(12)
                r_cap = p_cap.add_run(caption)
                r_cap.font.size = Pt(10)
                r_cap.font.italic = True
            except Exception as e:
                p_err = doc.add_paragraph(f"[{caption} - Path: {img_path}]")
                p_err.alignment = WD_ALIGN_PARAGRAPH.CENTER

    output_path = "LPJ_Tugas_Akhir_ShopSey_Android_Studio.docx"
    doc.save(output_path)
    print(f"Document saved successfully at: {os.path.abspath(output_path)}")

if __name__ == "__main__":
    create_document()
