import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

class Main {
    public static void main(String[] args) {
        String savePath = "d:\\Games\\savegames";
        String zipFileName = "saved_games.zip";

        GameProgress game1 = new GameProgress(1, 1, 3, 10.5);
        GameProgress game2 = new GameProgress(10, 2, 5, 12);
        GameProgress game3 = new GameProgress(3, 3, 6, 9.3);

        if (game1.saveGame(savePath + File.separator + "game1.dat")
                && game2.saveGame(savePath + File.separator + "game2.dat")
                && game3.saveGame(savePath + File.separator + "game3.dat")) {
            System.out.println("Игры сохранены.");
        }

        File dir = new File(savePath);
        boolean success = true;
        File zipFile = new File(savePath + File.separator + zipFileName);
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));) {
            for (File item : dir.listFiles()) {
                if (item.isFile() && !item.getName().equals(zipFileName)) {
                    // Добавить новую точку входа.
                    try (FileInputStream fis = new FileInputStream(item);) {
                        ZipEntry zipEntry = new ZipEntry(item.getName());
                        out.putNextEntry(zipEntry);
                        byte[] buffer = new byte[fis.available()];
                        fis.read(buffer); // считываем содержимое файла в массив byte
                        out.write(buffer); // добавляем содержимое к архиву
                        out.closeEntry();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        success = false;
                    } finally {
                        if (success) {
                            item.delete();
                        } else {
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            success = false;
        }
        System.out.println(success ? "Данные о сохранённых играх заархивированы." :
                "Архивация сохранённых игр завершилась с ошибкой.");
    }
}
