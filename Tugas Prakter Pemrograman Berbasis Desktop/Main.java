import java.util.*;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		// Daftar menu (array of Menu)
		Menu[] daftar = new Menu[] {
			new Menu("Nasi Kuning", 20000, "Makanan"),
			new Menu("Nasi Uduk", 25000, "Makanan"),
			new Menu("Sop Iga", 30000, "Makanan"),
            new Menu("Sate Kambing", 45000, "Makanan"),
			new Menu("Es Teh", 6000, "Minuman"),
			new Menu("Teh Panas", 6000, "Minuman"),
			new Menu("Jus Jeruk", 15000, "Minuman")
		};

		System.out.println("=== Daftar Menu Restoran ===");
		for (int i = 0; i < daftar.length; i++) {
			System.out.printf("%d. ", i+1);
			daftar[i].tampilkanMenu();
		}

		System.out.println();
		System.out.println("Masukkan pesanan (format: Nama Menu = jumlah). Maks 4 item. Ketik 'done' untuk selesai.");

		List<OrderItem> pesanan = new ArrayList<>();

		while (pesanan.size() < 4) {
			System.out.print("Pesanan ke-" + (pesanan.size()+1) + ": ");
			String line = sc.nextLine().trim();
			if (line.equalsIgnoreCase("done") || line.isEmpty()) break;

			// Expect format: Name = qty
			String[] parts = line.split("=");
			if (parts.length != 2) {
				System.out.println("Format salah. Gunakan: Nama Menu = jumlah");
				continue;
			}

			String nama = parts[0].trim();
			String qtyStr = parts[1].trim();
			int qty;
			try {
				qty = Integer.parseInt(qtyStr);
				if (qty <= 0) { System.out.println("Jumlah harus lebih dari 0"); continue; }
			} catch (NumberFormatException e) {
				System.out.println("Jumlah tidak valid");
				continue;
			}

			Menu found = findMenuByName(daftar, nama);
			if (found == null) {
				System.out.println("Menu tidak ditemukan: " + nama);
				continue;
			}

			pesanan.add(new OrderItem(found, qty));
		}

		if (pesanan.isEmpty()) {
			System.out.println("Tidak ada pesanan. Terima kasih.");
			sc.close();
			return;
		}

		// Hitung subtotal
		int subtotal = 0;
		for (OrderItem it : pesanan) subtotal += it.menu.harga * it.qty;

		// Hitung penawaran beli 1 gratis 1 untuk minuman (untuk setiap pasangan 2, 1 gratis)
		int drinkDiscount = 0;
		for (OrderItem it : pesanan) {
			if (it.menu.kategori.equalsIgnoreCase("Minuman")) {
				int free = it.qty / 2; // for every 2, one free
				drinkDiscount += free * it.menu.harga;
			}
		}

		int afterOffer = subtotal - drinkDiscount;

		// Diskon 10% jika > 100000
		double diskon10 = 0;
		if (afterOffer > 100000) {
			diskon10 = afterOffer * 0.10;
		}

		double afterDiscount = afterOffer - diskon10;

		// Pajak 10% dari total setelah diskon
		double pajak = afterDiscount * 0.10;

		// Biaya pelayanan tetap
		int service = 20000;

		double totalBayar = afterDiscount + pajak + service;

		// Cetak struk
		System.out.println();
		System.out.println("===== STRUK PEMBAYARAN =====");
		System.out.println("Item                      Jumlah   Harga    Total");
		for (OrderItem it : pesanan) {
			String itemName = it.menu.nama;
			int harga = it.menu.harga;
			int total = harga * it.qty;
			System.out.printf("%-25s %3d    %7d  %7d\n", itemName, it.qty, harga, total);
		}

		System.out.println("------------------------------------------------");
		System.out.printf("Subtotal: Rp %d\n", subtotal);
		if (drinkDiscount > 0) System.out.printf("Diskon Beli 1 Gratis 1 (minuman): -Rp %d\n", drinkDiscount);
		if (diskon10 > 0) System.out.printf("Diskon 10%% (subtotal>100000): -Rp %.0f\n", diskon10);
		System.out.printf("Subtotal setelah diskon: Rp %.0f\n", afterDiscount);
		System.out.printf("Pajak 10%%: Rp %.0f\n", pajak);
		System.out.printf("Biaya pelayanan: Rp %d\n", service);
		System.out.println("------------------------------------------------");
		System.out.printf("Total Bayar: Rp %.0f\n", totalBayar);
		System.out.println("Terima kasih telah memesan di restoran kami.");
		sc.close();
	}

	static Menu findMenuByName(Menu[] daftar, String nama) {
		for (Menu m : daftar) {
			if (m.nama.equalsIgnoreCase(nama)) return m;
		}
		return null;
	}

	static class OrderItem {
		Menu menu;
		int qty;
		OrderItem(Menu menu, int qty) { this.menu = menu; this.qty = qty; }
	}
}
