import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

class Musteri {
    int musteriID;
    String isim;
    String soyisim;
    LinkedList<Gonderi> gonderiGecmisi;

    public Musteri(int musteriID, String isim, String soyisim) {
        this.musteriID = musteriID;
        this.isim = isim;
        this.soyisim = soyisim;
        this.gonderiGecmisi = new LinkedList<>();
    }

    public void gonderiEkle(Gonderi yeniGonderi) {
        int i = 0;
        while (i < gonderiGecmisi.size() && gonderiGecmisi.get(i).tarih.before(yeniGonderi.tarih)) {
            i++;
        }
        gonderiGecmisi.add(i, yeniGonderi);
    }

    public void gonderiGecmisiniListele(JTextArea output) {
        if (gonderiGecmisi.isEmpty()) {
            output.setText("Gonderi gecmisi bos.");
        } else {
            StringBuilder sb = new StringBuilder();
            // Reverse order for displaying
            List<Gonderi> reversedList = new ArrayList<>(gonderiGecmisi);
            Collections.reverse(reversedList);
            for (Gonderi gonderi : reversedList) {
                sb.append("Gonderi ID: ").append(gonderi.gonderiID)
                        .append(", Tarih: ").append(gonderi.tarih)
                        .append(", Durum: ").append(gonderi.teslimDurumu)
                        .append(", Sure: ").append(gonderi.teslimSuresi).append(" gun\n");
            }
            output.setText(sb.toString());
        }
    }
}

class Gonderi {
    int gonderiID;
    Date tarih;
    String teslimDurumu;
    int teslimSuresi;

    public Gonderi(int gonderiID, Date tarih, String teslimDurumu, int teslimSuresi) {
        this.gonderiID = gonderiID;
        this.tarih = tarih;
        this.teslimDurumu = teslimDurumu;
        this.teslimSuresi = teslimSuresi;
    }
}

class Sehir {
    String sehirAdi;
    int sehirID;
    List<Sehir> altSehirler;
    int teslimSuresi;

    public Sehir(String sehirAdi, int sehirID, int teslimSuresi) {
        this.sehirAdi = sehirAdi;
        this.sehirID = sehirID;
        this.altSehirler = new ArrayList<>();
        this.teslimSuresi = teslimSuresi;
    }

    public void altSehirEkle(Sehir altSehir) {
        altSehirler.add(altSehir);
    }

    public void agaciGoruntule(String prefix, JTextArea output) {
        output.append(prefix + "- " + sehirAdi + " (Teslim Sure: " + teslimSuresi + " gun)\n");
        for (Sehir altSehir : altSehirler) {
            altSehir.agaciGoruntule(prefix + "    ", output);
        }
    }

    public void agaciCiz(Graphics g, int x, int y, int xOffset, int yOffset) {
        g.drawString(sehirAdi + " (" + teslimSuresi + " gun)", x, y);
        for (int i = 0; i < altSehirler.size(); i++) {
            int childX = x - xOffset / 2 + (xOffset / altSehirler.size()) * i;
            int childY = y + yOffset;
            g.drawLine(x, y + 5, childX, childY - 10);
            altSehirler.get(i).agaciCiz(g, childX, childY, xOffset / 2, yOffset);
        }
    }
}

class KargoTakipSistemi {
    List<Musteri> musteriler;
    Sehir merkez;

    public KargoTakipSistemi() {
        this.musteriler = new ArrayList<>();
        this.merkez = new Sehir("Merkez", 0, 5);
    }

    public void baslat() {
        JFrame frame = new JFrame("Kargo Takip Sistemi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("KARGO TAKIP SISTEMI", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Tahoma", Font.BOLD, 36));
        headerLabel.setForeground(new Color(0, 48, 73));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 15, 10));
        mainPanel.setBackground(new Color(234, 226, 183));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setBackground(new Color(234, 226, 183));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        Font font = new Font("Tahoma", Font.PLAIN, 16);
        Color buttonColor = new Color(0, 123, 255);

        JButton musteriEkleButton = createStyledButton("Yeni Musteri Ekle", font, buttonColor);
        JButton gonderiEkleButton = createStyledButton("Kargo Gonderimi Ekle", font, buttonColor);
        JButton gonderiListeleButton = createStyledButton("Gonderim Gecmisini Goruntule", font, buttonColor);
        JButton son5GonderiButton = createStyledButton("Son 5 Gonderiyi Goruntule", font, buttonColor);
        JButton teslimatRotaEkleButton = createStyledButton("Teslimat Rotalarini Olustur", font, buttonColor);
        JButton teslimatRotaGoruntuleButton = createStyledButton("Teslimat Rotalarini Goster", font, buttonColor);
        JTextArea output = new JTextArea();
        output.setFont(font);
        output.setEditable(false);
        output.setBackground(new Color(252, 191, 73));

        buttonPanel.add(musteriEkleButton, gbc);
        gbc.gridy++;
        buttonPanel.add(gonderiEkleButton, gbc);
        gbc.gridy++;
        buttonPanel.add(gonderiListeleButton, gbc);
        gbc.gridy++;
        buttonPanel.add(son5GonderiButton, gbc);
        gbc.gridy++;
        buttonPanel.add(teslimatRotaEkleButton, gbc);
        gbc.gridy++;
        buttonPanel.add(teslimatRotaGoruntuleButton, gbc);

        JScrollPane scrollPane = new JScrollPane(output);
        scrollPane.setPreferredSize(new Dimension(700, 150));

        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);

        // Action listeners remain unchanged
        musteriEkleButton.addActionListener(e -> {
            String isim = JOptionPane.showInputDialog("Musteri ismi:");
            String soyisim = JOptionPane.showInputDialog("Musteri soyismi:");
            int musteriID = Integer.parseInt(JOptionPane.showInputDialog("Musteri ID:"));
            musteriler.add(new Musteri(musteriID, isim, soyisim));
            output.setText("Yeni musteri eklendi: " + isim + " " + soyisim);
        });

        gonderiEkleButton.addActionListener(e -> {
            int musteriID = Integer.parseInt(JOptionPane.showInputDialog("Musteri ID:"));
            Musteri musteri = musteriler.stream()
                    .filter(m -> m.musteriID == musteriID)
                    .findFirst()
                    .orElse(null);

            if (musteri == null) {
                output.setText("Musteri bulunamadi.");
                return;
            }

            int gonderiID = Integer.parseInt(JOptionPane.showInputDialog("Gonderi ID:"));
            int teslimSuresi = Integer.parseInt(JOptionPane.showInputDialog("Teslim Suresi (gun):"));
            String teslimDurumu = JOptionPane.showInputDialog("Teslim Durumu (Teslim Edildi/Teslim Edilmedi):");
            Gonderi yeniGonderi = new Gonderi(gonderiID, new Date(), teslimDurumu, teslimSuresi);
            musteri.gonderiEkle(yeniGonderi);
            output.setText("Gonderi basariyla eklendi.");
        });

        gonderiListeleButton.addActionListener(e -> {
            int musteriID = Integer.parseInt(JOptionPane.showInputDialog("Musteri ID:"));
            Musteri musteri = musteriler.stream()
                    .filter(m -> m.musteriID == musteriID)
                    .findFirst()
                    .orElse(null);

            if (musteri == null) {
                output.setText("Musteri bulunamadi.");
            } else {
                JFrame gonderiGecmisiFrame = new JFrame("Gonderi Gecmisi");
                gonderiGecmisiFrame.setSize(500, 400);
                JTextArea gonderiOutput = new JTextArea();
                gonderiOutput.setFont(font);
                gonderiOutput.setEditable(false);
                gonderiGecmisiFrame.add(new JScrollPane(gonderiOutput));
                gonderiGecmisiFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                gonderiGecmisiFrame.setVisible(true);
                musteri.gonderiGecmisiniListele(gonderiOutput);
            }
        });

        son5GonderiButton.addActionListener(e -> {
            List<Gonderi> tumGonderiler = new ArrayList<>();
            for (Musteri musteri : musteriler) {
                tumGonderiler.addAll(musteri.gonderiGecmisi);
            }

            tumGonderiler.removeIf(g -> g.teslimDurumu.equalsIgnoreCase("Teslim Edildi"));
            tumGonderiler.sort(Comparator.comparingInt(g -> g.teslimSuresi));

            Stack<Gonderi> son5GonderiStack = new Stack<>();
            for (int i = 0; i < Math.min(5, tumGonderiler.size()); i++) {
                son5GonderiStack.push(tumGonderiler.get(i));
            }

            if (son5GonderiStack.isEmpty()) {
                output.setText("Teslim edilmemis gonderi bulunamadi.");
            } else {
                StringBuilder sb = new StringBuilder("Son 5 Gonderi:\n");
                while (!son5GonderiStack.isEmpty()) {
                    Gonderi gonderi = son5GonderiStack.pop();
                    sb.append("Gonderi ID: ").append(gonderi.gonderiID)
                            .append(", Tarih: ").append(gonderi.tarih)
                            .append(", Durum: ").append(gonderi.teslimDurumu)
                            .append(", Sure: ").append(gonderi.teslimSuresi).append(" gun\n");
                }
                output.setText(sb.toString());
            }
        });

        teslimatRotaEkleButton.addActionListener(e -> {
            String sehirAdi = JOptionPane.showInputDialog("Sehir Adi:");
            int sehirID = Integer.parseInt(JOptionPane.showInputDialog("Sehir ID:"));
            int teslimSuresi = Integer.parseInt(JOptionPane.showInputDialog("Teslim Suresi (gun):"));

            String bagliSehirAdi = JOptionPane.showInputDialog("Bagli oldugu sehir (Merkez icin 'Merkez' yazin):");
            Sehir bagliSehir = bagliSehirAdi.equals("Merkez") ? merkez :
                    merkez.altSehirler.stream().filter(s -> s.sehirAdi.equals(bagliSehirAdi)).findFirst().orElse(null);

            if (bagliSehir == null) {
                output.setText("Bagli sehir bulunamadi.");
                return;
            }

            Sehir yeniSehir = new Sehir(sehirAdi, sehirID, teslimSuresi);
            bagliSehir.altSehirEkle(yeniSehir);
            output.setText("Sehir basariyla eklendi: " + sehirAdi);
        });

        teslimatRotaGoruntuleButton.addActionListener(e -> {
            JFrame treeFrame = new JFrame("Teslimat Rotalari");
            treeFrame.setSize(800, 600);
            treeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel canvas = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    merkez.agaciCiz(g, getWidth() / 2, 50, getWidth() / 2, 100);
                }
            };

            treeFrame.add(canvas);
            treeFrame.setVisible(true);
        });
    }

    private JButton createStyledButton(String text, Font font, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(new Color(214, 40, 40));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(247, 127, 0), 2),
                BorderFactory.createEmptyBorder(7, 20, 7, 20)));
        button.setContentAreaFilled(true);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KargoTakipSistemi().baslat());
    }
}
