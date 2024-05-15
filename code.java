import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.ByteArrayInputStream;
import java.util.Vector;

public class SftpUtil {

    public static String listFiles(String host, String port, String privateKey) {
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            JSch jsch = new JSch();
            byte[] privateKeyBytes = privateKey.getBytes();
            jsch.addIdentity("privateKey", privateKeyBytes, null, null);

            session = jsch.getSession("username", host, Integer.parseInt(port));
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            Vector<ChannelSftp.LsEntry> fileList = channelSftp.ls(".");

            StringBuilder fileNames = new StringBuilder();
            for (ChannelSftp.LsEntry entry : fileList) {
                fileNames.append(entry.getFilename()).append("\n");
            }

            return fileNames.toString();
        } catch (JSchException | SftpException e) {
            return "Error: " + e.getMessage();
        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }

    public static void main(String[] args) {
        String host = "your_sftp_host";
        String port = "22";  // Default SFTP port
        String privateKey = "your_private_key_content";

        String fileList = listFiles(host, port, privateKey);
        System.out.println(fileList);
    }
}
