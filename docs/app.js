/**
 * ShopSey - Interactive Mobile Application Demo Engine
 * Emulates the Jetpack Compose Single-Activity Architecture
 */

// Application State
const state = {
  currentScreen: 'splash',
  previousScreen: null,
  activeTab: 0, // 0: Home, 1: Shop, 2: Search, 3: Bag, 4: Profile
  userEmail: 'member@shopsey.com',
  userName: 'R',
  currentOtp: '392817',
  otpTimer: 45,
  otpTimerInterval: null,
  cartCount: 1,
  cartQty: 1,
  cartUnitPrice: 3329000,
  discountPercent: 0,
  homeSectionIndex: 0,
  homeAutoTimer: null,
  homeTimerProgress: 0,
  landingSlideIndex: 0,
  landingAutoTimer: null,
  heroSlideIndex: 0,
  heroAutoTimer: null,
  selectedShoeSize: 'EU 42.5',
  currentDetailProduct: {
    title: "Air Jordan 4 'Toro'",
    category: "Men's Basketball Shoes",
    price: "Rp3.329.000",
    image: "assets/images/bag1.png",
    images: [
      "assets/images/bag1.png",
      "assets/images/detail1.png",
      "assets/images/detail2.png",
      "assets/images/detail3.png"
    ]
  }
};

// Home Brand Sections
const homeSections = [
  {
    id: "nike",
    brandName: "Nike",
    title: "Nike United",
    subtitle: "Created by athletes.",
    bg: "assets/images/homebg1.png",
    icon: "assets/images/icon_nike.png",
    price: "Rp2.899.000"
  },
  {
    id: "sheathkobe",
    brandName: "Sheath Kobe",
    title: "Kobe Protro",
    subtitle: "Passion, focus, and relentless work ethic.",
    bg: "assets/images/homebg2.png",
    icon: "assets/images/icon_sheathkobe.png",
    price: "Rp3.499.000"
  },
  {
    id: "adidas",
    brandName: "Adidas",
    title: "Predator Elite",
    subtitle: "Speed, precision, and surgical touch.",
    bg: "assets/images/homebg3.png",
    icon: "assets/images/icon_adidas.png",
    price: "Rp3.199.000"
  },
  {
    id: "puma",
    brandName: "Puma",
    title: "Puma Future 7",
    subtitle: "Play without boundaries. Agility and pure energy.",
    bg: "assets/images/homebg4.png",
    icon: "assets/images/icon_puma.png",
    price: "Rp2.499.000"
  },
  {
    id: "underarmour",
    brandName: "Under Armour",
    title: "Curry Flow 11",
    subtitle: "Change the game for good. Unstoppable grip.",
    bg: "assets/images/homebg5.png",
    icon: "assets/images/icon_underarmour.png",
    price: "Rp2.299.000"
  },
  {
    id: "jordan",
    brandName: "Jordan",
    title: "Jordan 6 Sneaker",
    subtitle: "Defy gravity. An iconic silhouette born for flight.",
    bg: "assets/images/homebg6.png",
    icon: "assets/images/icon_jordan.png",
    price: "Rp3.329.000"
  }
];

// Technical Inspector Screen Metadata
const screenMetadata = {
  'splash': {
    tag: 'COMPOSE SPLASH EFFECT',
    title: 'Splash Screen',
    desc: 'Layar awal pembuka dengan logo ShopSey bernuansa hitam elegan, dilengkapi efek breathing/scale animasi serta transisi otomatis ke Landing Screen.',
    components: 'SplashScreen.kt, Animatable, LaunchedEffect, Image',
    theme: 'Dark Cinematic (#000000)'
  },
  'landing': {
    tag: 'FULLSCREEN CROSSFADE CAROUSEL',
    title: 'Landing Screen',
    desc: 'Halaman selamat datang dengan background gambar model olahraga (landing1-6.png) yang bertransisi halus (Crossfade 1000ms) setiap 3 detik.',
    components: 'LandingScreen.kt, Crossfade, Box, Surface, Vignette Canvas',
    theme: 'Dark Ambient'
  },
  'email-signup': {
    tag: 'USER INPUT & VALIDATION',
    title: 'Email Input (Sign Up)',
    desc: 'Alur pendaftaran member baru: validasi format email interaktif dengan pengalihan menuju pembuatan kata sandi (CreatePasswordScreen).',
    components: 'EmailInputScreen.kt, OutlinedTextField, KeyboardOptions',
    theme: 'Light Clean (#FFFFFF)'
  },
  'email-signin': {
    tag: 'AUTH ROUTING (SIGN IN)',
    title: 'Email Input (Sign In)',
    desc: 'Alur masuk akun member terdaftar: setelah memasukkan email, sistem mengarahkan ke verifikasi kode OTP instan.',
    components: 'EmailInputScreen.kt, Single-Activity NavGraph, StateFlow',
    theme: 'Light Clean (#FFFFFF)'
  },
  'otp': {
    tag: 'LOCAL SYSTEM NOTIFICATION SIMULATION',
    title: 'OTP Verification Screen',
    desc: 'Simulasi pengiriman kode OTP 6-digit via Heads-Up System Notification Android di status bar dengan timer hitung mundur dan tombol salin otomatis.',
    components: 'OtpVerificationScreen.kt, NotificationManager, Heads-up Alert',
    theme: 'Light Clean (#FFFFFF)'
  },
  'password': {
    tag: 'SECURE AUTHENTICATION',
    title: 'Password Input Screen',
    desc: 'Input kata sandi dengan visibilitas toggle eye icon, link lupa sandi, dan opsi masuk via OTP alternatif.',
    components: 'PasswordInputScreen.kt, VisualTransformation, IconButton',
    theme: 'Light Clean (#FFFFFF)'
  },
  'create-password': {
    tag: 'PASSWORD POLICY VALIDATION',
    title: 'Create Password Screen',
    desc: 'Pembuatan kata sandi baru untuk alur registrasi dengan checklist pemenuhan kriteria keamanan sandi.',
    components: 'CreatePasswordScreen.kt, Regex Checker, OutlinedTextField',
    theme: 'Light Clean (#FFFFFF)'
  },
  'register-form': {
    tag: 'USER PROFILE ONBOARDING',
    title: 'Register Form Screen',
    desc: 'Formulir data diri member: Nama Depan, Nama Belakang, Tanggal Lahir, dan Pilihan Gender (Pria/Wanita/Lainnya).',
    components: 'RegisterFormScreen.kt, RadioButton, DatePicker, ScrollState',
    theme: 'Light Clean (#FFFFFF)'
  },
  'forgot-password': {
    tag: 'ACCOUNT RECOVERY',
    title: 'Forgot Password Screen',
    desc: 'Pemulihan kata sandi akun dengan verifikasi identitas email dan pembaruan kredensial.',
    components: 'ForgotPasswordScreen.kt, Button, TextFieldDefaults',
    theme: 'Light Clean (#FFFFFF)'
  },
  'login-success': {
    tag: 'SUCCESS FEEDBACK',
    title: 'Login Success Screen',
    desc: 'Layar ucapan selamat berhasil masuk dengan animasi centang hijau dan tombol lanjutkan.',
    components: 'LoginSuccessScreen.kt, Spring Animation, Surface',
    theme: 'Light Clean (#FFFFFF)'
  },
  'welcome': {
    tag: '3D ANGLED PHOTO COLLAGE',
    title: 'Welcome Screen',
    desc: 'Kolase 15 foto olahraga dengan kemiringan rotasi 30 derajat, vignette bayangan gelap, dan sapaan personal member ShopSey.',
    components: 'WelcomeScreen.kt, graphicsLayer(rotationZ = -30f), Vignette',
    theme: 'Dark Cinematic (#000000)'
  },
  'home': {
    tag: 'SNAP VERTICAL PAGER & 3D NAVBAR',
    title: 'Home Screen (Dark Mode)',
    desc: 'Tampilan utama sinematik dengan Vertical Pager 6 brand olahraga terkemuka, auto-scrolling timer bar, dan floating 3D glass navbar.',
    components: 'HomeScreen.kt, VerticalPager, rememberPagerState, FloatingNav',
    theme: 'Dark Cinematic (#000000)'
  },
  'shop': {
    tag: 'DYNAMIC CATALOG & BENTO GRID',
    title: 'Shop Catalog Screen',
    desc: 'Katalog produk lengkap dengan Carousel Banner, Mozaik Kategori Bento 2-kolom, Jump Back In, dan Lifestyle Picks 3-kolom grid.',
    components: 'ShopScreen.kt, HorizontalPager, BentoGrid, DropdownMenu',
    theme: 'Light Elevated (#F7F7F9)'
  },
  'search': {
    tag: 'LIVE KEYWORD FILTERING',
    title: 'Search Screen',
    desc: 'Pencarian produk instan dengan filter real-time, riwayat pencarian recent tags, dan rekomendasi kategori terpopuler.',
    components: 'SearchScreen.kt, BasicTextField, Filter Chips, LazyColumn',
    theme: 'Light Clean (#FFFFFF)'
  },
  'detail': {
    tag: 'EDGE-TO-EDGE PRODUCT GALLERY',
    title: 'Product Detail Screen',
    desc: 'Detail lengkap sepatu/pakaian dengan slider foto multi-angle tanpa margin, selector ukuran EU, tombol Add to Bag, dan accordion ulasan.',
    components: 'ProductDetailScreen.kt, HorizontalPager, SizePicker, Accordion',
    theme: 'Light Clean (#FFFFFF)'
  },
  'bag': {
    tag: 'CART & CHECKOUT SIMULATION',
    title: 'Shopping Bag Screen',
    desc: 'Keranjang belanja dengan pengubah jumlah (+/-), indikator gratis ongkir, voucher promo diskon 20%, dan kalkulasi total otomatis.',
    components: 'BagScreen.kt, QuantitySelector, CostBreakdown, CheckoutDialog',
    theme: 'Light Clean (#FFFFFF)'
  },
  'profile': {
    tag: 'MEMBER HUB & SETTINGS',
    title: 'User Profile Screen',
    desc: 'Pusat akun member ShopSey: avatar inisial, 4 menu aksi (Pesanan, Akses, Favorit, Pengaturan), daftar atlet yang diikuti, dan pengaturan profil.',
    components: 'ProfileScreen.kt, MemberTier, VerticalDivider, SettingsMenu',
    theme: 'Light Clean (#FFFFFF)'
  }
};

// Initialize Application
document.addEventListener('DOMContentLoaded', () => {
  setupClock();
  setupNavigation();
  setupLandingAutoCrossfade();
  setupHomeVerticalPager();
  setupShopHeroCarousel();
  setupOtpFlow();
  setupBagInteractions();
  setupSearchFilter();
  setupViewModeSwitcher();
  setupDeviceColorPicker();

  // Initial Screen
  navigateTo('splash');

  // Auto transition from Splash to Landing
  setTimeout(() => {
    if (state.currentScreen === 'splash') {
      navigateTo('landing');
    }
  }, 2400);
});

// Live Phone Clock
function setupClock() {
  const timeElements = document.querySelectorAll('.clock-time');
  function updateTime() {
    const now = new Date();
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    timeElements.forEach(el => el.textContent = `${hours}:${minutes}`);
  }
  updateTime();
  setInterval(updateTime, 10000);
}

// Master Navigation Method
function navigateTo(screenId) {
  state.previousScreen = state.currentScreen;
  state.currentScreen = screenId;

  // Deactivate all screens
  document.querySelectorAll('.screen').forEach(s => s.classList.remove('active'));

  // Activate target screen
  const targetScreen = document.getElementById(`screen-${screenId}`);
  if (targetScreen) {
    targetScreen.classList.add('active');
    targetScreen.scrollTop = 0;
  }

  // Determine Light vs Dark Theme
  const isDark = (screenId === 'splash' || screenId === 'landing' || screenId === 'welcome' || screenId === 'home');
  const phoneScreen = document.querySelector('.phone-screen');
  const floatingNav = document.getElementById('floating-bottom-nav');

  if (isDark) {
    phoneScreen.classList.remove('light-theme');
    if (floatingNav) {
      floatingNav.classList.remove('light-style');
      floatingNav.classList.add('dark-style');
    }
  } else {
    phoneScreen.classList.add('light-theme');
    if (floatingNav) {
      floatingNav.classList.remove('dark-style');
      floatingNav.classList.add('light-style');
    }
  }

  // Floating Nav Visibility: only on main tabs (home, shop, search, bag, profile)
  const isMainTab = ['home', 'shop', 'search', 'bag', 'profile'].includes(screenId);
  if (floatingNav) {
    floatingNav.style.display = isMainTab ? 'flex' : 'none';
  }

  // Update Active Tab Index
  const tabMap = { 'home': 0, 'shop': 1, 'search': 2, 'bag': 3, 'profile': 4 };
  if (tabMap[screenId] !== undefined) {
    state.activeTab = tabMap[screenId];
    updateNavTabSelection(state.activeTab);
  }

  // Update Left Sidebar Active Button
  document.querySelectorAll('.screen-link-btn').forEach(btn => {
    btn.classList.toggle('active', btn.dataset.screen === screenId);
  });

  // Update Technical Inspector
  updateInspector(screenId);

  // If entering Home, activate timer
  if (screenId === 'home') {
    startHomeTimer();
  } else {
    stopHomeTimer();
  }
}

// Update Bottom Nav Tab Selection
function updateNavTabSelection(tabIndex) {
  document.querySelectorAll('.nav-tab-item').forEach((item, idx) => {
    item.classList.toggle('active', idx === tabIndex);
  });
}

// Update Technical Inspector Panel
function updateInspector(screenId) {
  const meta = screenMetadata[screenId] || {
    tag: 'JETPACK COMPOSE COMPONENT',
    title: screenId,
    desc: 'Halaman antarmuka aplikasi ShopSey berbasis Android Jetpack Compose.',
    components: 'Composable Function, State Management',
    theme: 'Dynamic'
  };

  const tagEl = document.getElementById('inspect-tag');
  const titleEl = document.getElementById('inspect-title');
  const descEl = document.getElementById('inspect-desc');
  const compEl = document.getElementById('inspect-comp');
  const themeEl = document.getElementById('inspect-theme');

  if (tagEl) tagEl.textContent = meta.tag;
  if (titleEl) titleEl.textContent = meta.title;
  if (descEl) descEl.textContent = meta.desc;
  if (compEl) compEl.textContent = meta.components;
  if (themeEl) themeEl.textContent = meta.theme;
}

// Setup Event Listeners for Nav Links
function setupNavigation() {
  // Sidebar Screen Jump Buttons
  document.querySelectorAll('.screen-link-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      const target = btn.dataset.screen;
      if (target) navigateTo(target);
    });
  });

  // Floating Nav Bar Tab Click
  document.querySelectorAll('.nav-tab-item').forEach(item => {
    item.addEventListener('click', () => {
      const tab = parseInt(item.dataset.tab);
      const screens = ['home', 'shop', 'search', 'bag', 'profile'];
      if (screens[tab]) {
        navigateTo(screens[tab]);
      }
    });
  });

  // Splash click to skip
  const splash = document.getElementById('screen-splash');
  if (splash) {
    splash.addEventListener('click', () => navigateTo('landing'));
  }

  // Welcome click to enter home
  const welcome = document.getElementById('screen-welcome');
  if (welcome) {
    welcome.addEventListener('click', () => navigateTo('home'));
  }

  // Restart Button
  const restartBtn = document.getElementById('btn-restart-app');
  if (restartBtn) {
    restartBtn.addEventListener('click', () => {
      navigateTo('splash');
      showToast('Aplikasi di-restart ke Splash Screen');
    });
  }
}

// Landing Background Carousel
function setupLandingAutoCrossfade() {
  const slides = document.querySelectorAll('.landing-slide-img');
  const dots = document.querySelectorAll('.landing-dot');
  if (!slides.length) return;

  function setSlide(index) {
    state.landingSlideIndex = index;
    slides.forEach((s, idx) => s.classList.toggle('active', idx === index));
    dots.forEach((d, idx) => d.classList.toggle('active', idx === index));
  }

  state.landingAutoTimer = setInterval(() => {
    if (state.currentScreen === 'landing') {
      const next = (state.landingSlideIndex + 1) % slides.length;
      setSlide(next);
    }
  }, 3500);

  dots.forEach(dot => {
    dot.addEventListener('click', () => {
      setSlide(parseInt(dot.dataset.slide));
    });
  });
}

// Home Screen Vertical Pager with Auto-Scroll & Timer Bar
function setupHomeVerticalPager() {
  const slides = document.querySelectorAll('.home-section-slide');
  const total = slides.length;

  function renderHomeSection(index) {
    state.homeSectionIndex = (index + total) % total;
    slides.forEach((s, idx) => s.classList.toggle('active', idx === state.homeSectionIndex));

    // Update Section Indicator (e.g., 01 / 06)
    const label = document.getElementById('home-timer-num');
    if (label) {
      const curStr = String(state.homeSectionIndex + 1).padStart(2, '0');
      const totStr = String(total).padStart(2, '0');
      label.textContent = `${curStr} / ${totStr}`;
    }

    // Reset progress bar
    const fill = document.getElementById('home-timer-fill');
    if (fill) fill.style.width = '0%';
    state.homeTimerProgress = 0;
  }

  // Next / Prev button arrows
  const prevBtn = document.getElementById('btn-home-prev');
  const nextBtn = document.getElementById('btn-home-next');

  if (prevBtn) {
    prevBtn.addEventListener('click', () => {
      renderHomeSection(state.homeSectionIndex - 1);
    });
  }
  if (nextBtn) {
    nextBtn.addEventListener('click', () => {
      renderHomeSection(state.homeSectionIndex + 1);
    });
  }

  // Mouse wheel scroll inside home screen
  const homeScreen = document.getElementById('screen-home');
  let wheelLock = false;
  if (homeScreen) {
    homeScreen.addEventListener('wheel', (e) => {
      if (wheelLock) return;
      wheelLock = true;
      if (e.deltaY > 20) {
        renderHomeSection(state.homeSectionIndex + 1);
      } else if (e.deltaY < -20) {
        renderHomeSection(state.homeSectionIndex - 1);
      }
      setTimeout(() => { wheelLock = false; }, 600);
    });
  }

  // Explore buttons on each section
  document.querySelectorAll('.home-explore-btn').forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.stopPropagation();
      const sec = homeSections[state.homeSectionIndex] || homeSections[0];
      openProductDetail({
        title: sec.title,
        category: sec.brandName,
        price: sec.price,
        image: sec.bg
      });
    });
  });

  // Touch Swipe on mobile
  let touchStartY = 0;
  if (homeScreen) {
    homeScreen.addEventListener('touchstart', (e) => {
      touchStartY = e.touches[0].clientY;
    }, { passive: true });

    homeScreen.addEventListener('touchend', (e) => {
      const touchEndY = e.changedTouches[0].clientY;
      const diff = touchStartY - touchEndY;
      if (Math.abs(diff) > 40) {
        if (diff > 0) {
          renderHomeSection(state.homeSectionIndex + 1);
        } else {
          renderHomeSection(state.homeSectionIndex - 1);
        }
      }
    }, { passive: true });
  }

  renderHomeSection(0);
}

function startHomeTimer() {
  stopHomeTimer();
  const fill = document.getElementById('home-timer-fill');
  state.homeTimerProgress = 0;

  state.homeAutoTimer = setInterval(() => {
    state.homeTimerProgress += 2;
    if (fill) fill.style.width = `${state.homeTimerProgress}%`;

    if (state.homeTimerProgress >= 100) {
      state.homeTimerProgress = 0;
      const slides = document.querySelectorAll('.home-section-slide');
      const nextIndex = (state.homeSectionIndex + 1) % slides.length;
      slides.forEach((s, idx) => s.classList.toggle('active', idx === nextIndex));
      state.homeSectionIndex = nextIndex;

      const label = document.getElementById('home-timer-num');
      if (label) {
        const curStr = String(nextIndex + 1).padStart(2, '0');
        label.textContent = `${curStr} / 06`;
      }
    }
  }, 90);
}

function stopHomeTimer() {
  if (state.homeAutoTimer) {
    clearInterval(state.homeAutoTimer);
    state.homeAutoTimer = null;
  }
}

// Shop Screen Hero Carousel
function setupShopHeroCarousel() {
  const slides = document.querySelectorAll('.hero-slide');
  const dots = document.querySelectorAll('.hero-dot');
  if (!slides.length) return;

  function setHeroSlide(idx) {
    state.heroSlideIndex = idx;
    slides.forEach((s, i) => s.classList.toggle('active', i === idx));
    dots.forEach((d, i) => d.classList.toggle('active', i === idx));
  }

  state.heroAutoTimer = setInterval(() => {
    if (state.currentScreen === 'shop') {
      const next = (state.heroSlideIndex + 1) % slides.length;
      setHeroSlide(next);
    }
  }, 4000);

  dots.forEach(dot => {
    dot.addEventListener('click', () => {
      setHeroSlide(parseInt(dot.dataset.hero));
    });
  });

  // Clicking lifestyle products opens detail
  document.querySelectorAll('.product-mini-card').forEach(card => {
    card.addEventListener('click', () => {
      const name = card.querySelector('.mini-card-name')?.textContent || 'Sneaker';
      const price = card.querySelector('.mini-card-price')?.textContent || 'Rp1.499.000';
      const img = card.querySelector('.mini-card-img')?.src || 'assets/images/shop4.png';

      openProductDetail({
        title: name,
        category: "Sport & Lifestyle",
        price: price,
        image: img
      });
    });
  });
}

// Open Product Detail Screen
function openProductDetail(product) {
  state.currentDetailProduct = {
    ...state.currentDetailProduct,
    ...product
  };

  const titleEl = document.getElementById('detail-title');
  const catEl = document.getElementById('detail-cat');
  const priceEl = document.getElementById('detail-price');
  const imgEl = document.getElementById('detail-main-img');

  if (titleEl) titleEl.textContent = state.currentDetailProduct.title;
  if (catEl) catEl.textContent = state.currentDetailProduct.category;
  if (priceEl) priceEl.textContent = state.currentDetailProduct.price;
  if (imgEl && product.image) imgEl.src = product.image;

  navigateTo('detail');
}

// OTP Flow & Simulated Push Notification
function setupOtpFlow() {
  // Generate random 6-digit OTP
  function generateOtp() {
    state.currentOtp = Math.floor(100000 + Math.random() * 900000).toString();
    const otpCodeEl = document.getElementById('notif-otp-val');
    if (otpCodeEl) otpCodeEl.textContent = state.currentOtp;
  }

  // Trigger Notification
  window.triggerOtpNotification = function() {
    generateOtp();
    const notif = document.getElementById('heads-up-notif');
    if (notif) {
      notif.classList.add('visible');
      // Auto hide after 8 seconds
      setTimeout(() => {
        notif.classList.remove('visible');
      }, 8000);
    }
    showToast(`Notifikasi sistem dikirim: OTP ${state.currentOtp}`);
  };

  // Test Notification Button in Toolbar
  const testNotifBtn = document.getElementById('btn-test-notif');
  if (testNotifBtn) {
    testNotifBtn.addEventListener('click', () => {
      triggerOtpNotification();
    });
  }

  // Copy Code Button in Notification
  const copyBtn = document.getElementById('btn-copy-otp');
  if (copyBtn) {
    copyBtn.addEventListener('click', () => {
      fillOtpInputs(state.currentOtp);
      showToast(`Kode ${state.currentOtp} berhasil disalin!`);
      const notif = document.getElementById('heads-up-notif');
      if (notif) notif.classList.remove('visible');
    });
  }

  // 6-Digit Boxes Input Auto-advance
  const otpBoxes = document.querySelectorAll('.otp-box');
  otpBoxes.forEach((box, idx) => {
    box.addEventListener('input', (e) => {
      const val = e.target.value;
      if (val.length === 1 && idx < otpBoxes.length - 1) {
        otpBoxes[idx + 1].focus();
      }
    });

    box.addEventListener('keydown', (e) => {
      if (e.key === 'Backspace' && !box.value && idx > 0) {
        otpBoxes[idx - 1].focus();
      }
    });
  });

  function fillOtpInputs(code) {
    otpBoxes.forEach((box, i) => {
      box.value = code[i] || '';
    });
    if (otpBoxes[5]) otpBoxes[5].focus();
  }

  // OTP Countdown Timer
  function startOtpCountdown() {
    state.otpTimer = 45;
    const timerEl = document.getElementById('otp-timer-count');
    if (state.otpTimerInterval) clearInterval(state.otpTimerInterval);

    state.otpTimerInterval = setInterval(() => {
      state.otpTimer--;
      if (timerEl) timerEl.textContent = `${state.otpTimer}d`;
      if (state.otpTimer <= 0) {
        clearInterval(state.otpTimerInterval);
        if (timerEl) timerEl.textContent = 'Kirim Ulang';
      }
    }, 1000);
  }

  // Trigger notification whenever user navigates to OTP screen
  const origNavigate = navigateTo;
  window.navigateTo = function(screenId) {
    origNavigate(screenId);
    if (screenId === 'otp') {
      setTimeout(() => {
        triggerOtpNotification();
        startOtpCountdown();
      }, 500);
    }
  };
}

// Shopping Bag Calculations & Promo Code
function setupBagInteractions() {
  const qtyEl = document.getElementById('bag-qty-val');
  const subtotalEl = document.getElementById('bag-subtotal');
  const taxEl = document.getElementById('bag-tax');
  const totalEl = document.getElementById('bag-total');
  const promoInput = document.getElementById('promo-input-field');
  const promoBtn = document.getElementById('btn-apply-promo');

  function recalculateCart() {
    const unit = state.cartUnitPrice;
    const qty = state.cartQty;
    let subtotal = unit * qty;

    if (state.discountPercent > 0) {
      subtotal = subtotal * (1 - state.discountPercent / 100);
    }

    const tax = Math.round(subtotal * 0.11);
    const total = subtotal + tax;

    if (qtyEl) qtyEl.textContent = qty;
    if (subtotalEl) subtotalEl.textContent = formatRupiah(subtotal);
    if (taxEl) taxEl.textContent = formatRupiah(tax);
    if (totalEl) totalEl.textContent = formatRupiah(total);

    // Update bottom nav badge
    updateCartBadge(qty);
  }

  const minusBtn = document.getElementById('btn-qty-minus');
  const plusBtn = document.getElementById('btn-qty-plus');

  if (minusBtn) {
    minusBtn.addEventListener('click', () => {
      if (state.cartQty > 1) {
        state.cartQty--;
        recalculateCart();
      }
    });
  }

  if (plusBtn) {
    plusBtn.addEventListener('click', () => {
      state.cartQty++;
      recalculateCart();
    });
  }

  // Promo Code Apply
  if (promoBtn) {
    promoBtn.addEventListener('click', () => {
      const code = (promoInput?.value || '').trim().toUpperCase();
      if (code === 'SHOPSEY20') {
        state.discountPercent = 20;
        recalculateCart();
        showToast('🎉 Diskon 20% kode SHOPSEY20 berhasil diterapkan!');
      } else {
        showToast('Kode voucher tidak valid. Coba: SHOPSEY20');
      }
    });
  }

  // Checkout Button opens celebratory modal
  const checkoutBtn = document.getElementById('btn-checkout-action');
  if (checkoutBtn) {
    checkoutBtn.addEventListener('click', () => {
      openModal('Pesanan Berhasil Dibuat!', 'Terima kasih telah berbelanja di ShopSey! Konfirmasi pesanan dan nomor pelacakan telah dikirim ke email Anda.');
    });
  }

  // Size Selector in Detail Screen
  document.querySelectorAll('.size-chip-box').forEach(chip => {
    chip.addEventListener('click', () => {
      document.querySelectorAll('.size-chip-box').forEach(c => c.classList.remove('selected'));
      chip.classList.add('selected');
      state.selectedShoeSize = chip.textContent.trim();
    });
  });

  // Add to Bag Button
  const addToBagBtn = document.getElementById('btn-add-to-bag');
  if (addToBagBtn) {
    addToBagBtn.addEventListener('click', () => {
      state.cartQty++;
      recalculateCart();
      showToast(`✓ ${state.currentDetailProduct.title} (${state.selectedShoeSize}) ditambahkan ke Bag!`);
    });
  }

  // Accordion Items in Detail
  document.querySelectorAll('.accordion-item').forEach(item => {
    item.addEventListener('click', () => {
      item.classList.toggle('open');
    });
  });

  recalculateCart();
}

function updateCartBadge(count) {
  state.cartCount = count;
  const badge = document.getElementById('nav-bag-badge');
  if (badge) {
    badge.textContent = count;
    badge.style.display = count > 0 ? 'flex' : 'none';
  }
}

// Live Search Filter
function setupSearchFilter() {
  const searchInput = document.getElementById('search-input-box');
  const clearBtn = document.getElementById('search-clear-btn');
  const chips = document.querySelectorAll('.search-chip');
  const productCards = document.querySelectorAll('.search-result-card');
  const countLabel = document.getElementById('search-results-count');

  function filterCatalog(query) {
    query = query.toLowerCase().trim();
    let matches = 0;

    productCards.forEach(card => {
      const name = card.dataset.name?.toLowerCase() || '';
      const cat = card.dataset.cat?.toLowerCase() || '';
      const isMatch = !query || name.includes(query) || cat.includes(query);
      card.style.display = isMatch ? 'flex' : 'none';
      if (isMatch) matches++;
    });

    if (countLabel) {
      countLabel.textContent = `${matches} Produk Ditemukan`;
    }

    if (clearBtn) {
      clearBtn.classList.toggle('visible', query.length > 0);
    }
  }

  if (searchInput) {
    searchInput.addEventListener('input', (e) => {
      filterCatalog(e.target.value);
    });
  }

  if (clearBtn) {
    clearBtn.addEventListener('click', () => {
      if (searchInput) {
        searchInput.value = '';
        searchInput.focus();
      }
      filterCatalog('');
    });
  }

  chips.forEach(chip => {
    chip.addEventListener('click', () => {
      const val = chip.textContent.trim();
      if (searchInput) searchInput.value = val;
      filterCatalog(val);
    });
  });
}

// View Mode Switcher: Simulator vs All Screens Overview vs Documentation
function setupViewModeSwitcher() {
  const btnSimulator = document.getElementById('btn-mode-simulator');
  const btnOverview = document.getElementById('btn-mode-overview');
  const btnDocs = document.getElementById('btn-mode-docs');

  const viewSimulator = document.getElementById('view-simulator');
  const viewOverview = document.getElementById('view-overview');
  const viewDocs = document.getElementById('view-docs');

  function setMode(mode) {
    [btnSimulator, btnOverview, btnDocs].forEach(b => b?.classList.remove('active'));
    [viewSimulator, viewOverview, viewDocs].forEach(v => {
      if (v) v.style.display = 'none';
      if (v) v.classList.remove('active');
    });

    if (mode === 'simulator') {
      btnSimulator?.classList.add('active');
      if (viewSimulator) viewSimulator.style.display = 'grid';
    } else if (mode === 'overview') {
      btnOverview?.classList.add('active');
      if (viewOverview) {
        viewOverview.style.display = 'block';
        viewOverview.classList.add('active');
      }
    } else if (mode === 'docs') {
      btnDocs?.classList.add('active');
      if (viewDocs) {
        viewDocs.style.display = 'block';
        viewDocs.classList.add('active');
      }
    }
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  btnSimulator?.addEventListener('click', () => setMode('simulator'));
  btnOverview?.addEventListener('click', () => setMode('overview'));
  btnDocs?.addEventListener('click', () => setMode('docs'));

  // Clicking any card in overview jumps to simulator with that screen
  document.querySelectorAll('.mosaic-screen-card').forEach(card => {
    card.addEventListener('click', () => {
      const targetScreen = card.dataset.target;
      setMode('simulator');
      if (targetScreen) navigateTo(targetScreen);
    });
  });
}

// Device Color Picker
function setupDeviceColorPicker() {
  const mockup = document.getElementById('phone-mockup-frame');
  const dots = document.querySelectorAll('.color-dot');

  dots.forEach(dot => {
    dot.addEventListener('click', () => {
      dots.forEach(d => d.classList.remove('active'));
      dot.classList.add('active');
      const color = dot.dataset.color;

      if (mockup) {
        mockup.classList.remove('silver-frame', 'blue-frame');
        if (color === 'silver') mockup.classList.add('silver-frame');
        if (color === 'blue') mockup.classList.add('blue-frame');
      }
    });
  });
}

// Global Modal Helper
function openModal(title, desc) {
  const modal = document.getElementById('global-modal');
  const titleEl = document.getElementById('modal-title');
  const descEl = document.getElementById('modal-desc');

  if (titleEl) titleEl.textContent = title;
  if (descEl) descEl.textContent = desc;
  if (modal) modal.classList.add('active');
}

window.closeModal = function() {
  const modal = document.getElementById('global-modal');
  if (modal) modal.classList.remove('active');
};

// Global Toast Message
function showToast(msg) {
  let toast = document.getElementById('toast-feedback');
  if (!toast) {
    toast = document.createElement('div');
    toast.id = 'toast-feedback';
    toast.className = 'toast-feedback';
    document.body.appendChild(toast);
  }
  toast.textContent = msg;
  toast.classList.add('visible');
  setTimeout(() => {
    toast.classList.remove('visible');
  }, 2600);
}

// Format IDR Rupiah
function formatRupiah(number) {
  return new Intl.NumberFormat('id-ID', {
    style: 'currency',
    currency: 'IDR',
    minimumFractionDigits: 0
  }).format(number);
}

