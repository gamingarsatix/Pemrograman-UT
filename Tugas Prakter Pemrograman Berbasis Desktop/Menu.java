public class Menu {
    String nama;
    int harga;
    String kategori;

    public Menu(String nama, int harga, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    public void tampilkanMenu() {
        System.out.println(nama + " - Rp" + harga + " (" + kategori + ")");
    }
}
