import java.io.*;
import java.util.Base64;
import java.util.Scanner;
import java.util.Arrays;

public class B64 {
    private static final String RED = "\033[0;31m";
    private static final String BLUE = "\033[0;34m";
    private static final String GREEN = "\033[0;32m";
    private static final String RESET = "\033[0m";

    public static void encodeText(String text, String outputFile) {
        String encoded = Base64.getEncoder().encodeToString(text.getBytes());
        if (outputFile != null) {
            try (FileWriter writer = new FileWriter(outputFile)) {
                writer.write(encoded);
                System.out.println(GREEN + "编码后的 Base64 已保存到: " + outputFile + RESET);
            } catch (IOException e) {
                System.out.println(RED + "Error: 写入文件失败 - " + e.getMessage() + RESET);
            }
        } else {
            System.out.println(GREEN + "编码后的 Base64:\n" + RESET + encoded);
        }
    }

    public static void encodeFile(String filePath, String outputFile) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            System.out.println(RED + "Error: 文件不存在。" + RESET);
            return;
        }
        try (FileInputStream fis = new FileInputStream(file);
             BufferedWriter writer = outputFile != null ? new BufferedWriter(new FileWriter(outputFile), 3 * 1024) : null) {

            Base64.Encoder encoder = Base64.getEncoder();
            byte[] buffer = new byte[3 * 1024];  // 设置3KB缓冲区
            int bytesRead;
            StringBuilder encodedChunk = new StringBuilder();
    
            while ((bytesRead = fis.read(buffer)) != -1) {
                String encoded = encoder.encodeToString(Arrays.copyOf(buffer, bytesRead));  // 分块编码
                if (writer != null) {
                    writer.write(encoded);  // 写入到输出文件
                } else {
                    encodedChunk.append(encoded);  // 保存到StringBuilder
                }
            }

            if (outputFile != null) {
                System.out.println(GREEN + "编码后的 Base64 已保存到: " + outputFile + RESET);
            } else {
                System.out.println(GREEN + "编码后的 Base64:\n" + RESET + encodedChunk.toString());
            }

        } catch (IOException e) {
            System.out.println(RED + "Error: 处理文件失败 - " + e.getMessage() + RESET);
        }
    }



    public static void decodeBase64(String base64String, String outputFile) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64String);
            if (outputFile != null) {
                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    fos.write(decodedBytes);
                    System.out.println(GREEN + "文件已保存到: " + outputFile + RESET);
                } catch (IOException e) {
                    System.out.println(RED + "Error: 写入文件失败 - " + e.getMessage() + RESET);
                }
            } else {
                System.out.println(GREEN + "解码后内容:\n" + RESET + new String(decodedBytes));
            }
        } catch (IllegalArgumentException e) {
            System.out.println(RED + "Error: 解码出错 - " + e.getMessage() + RESET);
        }
    }

        public static void decodeBase64FromFile(String inputFile, String outputFile) {
        File file = new File(inputFile);
        if (!file.exists() || !file.isFile()) {
            System.out.println(RED + "Error: 输入的文件不存在。" + RESET);
            return;
        }
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file), 3 * 1024);
             BufferedOutputStream outputStream = outputFile != null ? new BufferedOutputStream(new FileOutputStream(outputFile), 3 * 1024) : null) {

            byte[] buffer = new byte[3 * 1024]; // 3KB缓冲区
            byte[] base64Chunk = new byte[4]; // 用于存储 Base64 编码的 4 字节
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                String base64String = new String(buffer, 0, bytesRead);
                // 对读取的每个 Base64 字符串进行解码
                byte[] decodedBytes = Base64.getDecoder().decode(base64String);
                if (outputStream != null) {
                    outputStream.write(decodedBytes); // 直接写入输出流
                }
            }
        } catch (IOException e) {
            System.out.println(RED + "Error: 读取文件失败 - " + e.getMessage() + RESET);
        }
    }




    public static void printUsage() {
        System.out.println(RED + "用法:");
        System.out.println(GREEN + "编码：");
        System.out.println("encode -ft text_or_fileName path/to/output.txt");
        System.out.println(" ");
        System.out.println(" -f:对文件进行编码");
        System.out.println(" -t:对文本进行编码");
        System.out.println(" text_or_fileName:若之前指定了-f选项，则输入文件名；若指定的是-t选项，此处应为文本");
        System.out.println(" path/to/output.txt:将编码输出保存为文本文件。若不指定路径，则会保存到程序的同目录下" + RESET);
        System.out.println(" ");
        System.out.println(BLUE + "解码");
        System.out.println("decode -f input.txt_or_base64String output.bin");
        System.out.println(" ");
        System.out.println(" -f:从文本文件读取base64字符串并解码");
        System.out.println(" input.txt_or_base64String:指定读取base64字符串的文本文件（可指定路径），或直接输入base64字符串。");
        System.out.println("*注意：当直接输入base64字符串进行解码时，不要输入第二个-f选项");
        System.out.println(" ");
        System.out.println(" output.bin:指定将输出结果保存为文件（可选）。根据不同情况，输出的文件可能并不是二进制文件，而是纯文本文件，请根据实际情况调整文件扩展名。" + RESET);
        System.out.println(" ");
    }

    public static void processCommand(String command) {
        String[] parts = command.split("\\s+", 4);
        if (parts.length < 2) {
            System.out.println(RED + "错误: 命令不完整。" + RESET);
            printUsage();
            return;
        }
        String mode = parts[0];
        if (mode.equals("encode")) {
            if (parts.length < 3) {
                System.out.println(RED + "错误: 缺少选项。" + RESET);
                printUsage();
                return;
            }
            String option = parts[1];
            if (option.equals("-t")) {
                if (parts.length == 3) {
                    encodeText(parts[2], null);
                } else {
                    encodeText(parts[2], parts[3]);
                }
            } else if (option.equals("-f")) {
                if (parts.length == 3) {
                    encodeFile(parts[2], null);
                } else {
                    encodeFile(parts[2], parts[3]);
                }
            } else {
                System.out.println(RED + "错误: 无效的选项。" + RESET);
                printUsage();
            }
        } else if (mode.equals("decode")) {
            if (parts[1].equals("-f")) {
                if (parts.length == 4) {
                    decodeBase64FromFile(parts[2], parts[3]);
                } else if (parts.length == 3) {
                    decodeBase64FromFile(parts[2], null);
                } else {
                    System.out.println(RED + "用法: decode -f [path/to/input.txt] [path/to/output.bin]" + RESET);
                }
            } else {
                if (parts.length == 3) {
                    decodeBase64(parts[1], parts[2]);
                } else if (parts.length == 2) {
                    decodeBase64(parts[1], null);
                } else {
                    System.out.println(RED + "用法: decode [base64String] [path/to/output.bin]" + RESET);
                }
            }
        } else {
            System.out.println(RED + "错误: 无效的模式。" + RESET);
            printUsage();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(GREEN + "base64编解码工具。输入 'exit' 退出。" + RESET);

        while (true) {
            System.out.print("请输入命令: \n");
            String command = scanner.nextLine().trim();
            if (command.equalsIgnoreCase("exit")) {
                System.out.println(GREEN + "退出程序。" + RESET);
                break;
            }
            processCommand(command);
        }
        scanner.close();
    }
}
