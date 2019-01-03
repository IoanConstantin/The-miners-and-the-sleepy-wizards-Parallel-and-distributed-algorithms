import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Class for a miner.
 */
public class Miner extends Thread {

	Integer hashCount;
	static Set<Integer> solved = new HashSet<Integer>();
	CommunicationChannel channel;

	/**
	 * Creates a {@code Miner} object.
	 * 
	 * @param hashCount
	 *            number of times that a miner repeats the hash operation when
	 *            solving a puzzle.
	 * @param solved
	 *            set containing the IDs of the solved rooms
	 * @param channel
	 *            communication channel between the miners and the wizards
	 */
	public Miner(Integer hashCount, Set<Integer> solved, CommunicationChannel channel) {
		this.hashCount = hashCount;
		this.solved = solved;
		this.channel = channel;
	}

	public static String encryptThisString(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            
            // convert to string
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
            String hex = Integer.toHexString(0xff & messageDigest[i]);
            if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
    
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

	@Override
	public void run() {	
		while(true)
		{
			Message mesajcifrat=new Message(1,"a");
			Message msg1=new Message(1,"a");
			Message msg2=new Message(1,"a");
			int flagEND=0;
			int parinte=0;
			int CameraRezolvata=-2;

			synchronized(solved)
			{
				msg1=channel.getMessageWizardChannel();

				if(msg1.getData().equals("END")&&msg1.getCurrentRoom()==-1)
					flagEND=1;

				if(flagEND==0)
				{
					msg2 = channel.getMessageWizardChannel();
				}
			}

			parinte=msg1.getCurrentRoom();

			if(msg1.getData().equals("END")&&msg1.getCurrentRoom()==-1)
				flagEND=1;

			if(flagEND==0)
			{
				if(solved.contains(msg2.getCurrentRoom())==false)
				{
					for(int j=0;j<hashCount;j++) 
					{
						msg2.setData(encryptThisString(msg2.getData()));
					}

					mesajcifrat.setParentRoom(parinte);	
					mesajcifrat.setCurrentRoom(msg2.getCurrentRoom());
					mesajcifrat.setData(msg2.getData());

					CameraRezolvata=msg2.getCurrentRoom();
					solved.add(CameraRezolvata);
					channel.putMessageMinerChannel(mesajcifrat);
				}
			}

			flagEND=0;
		}
	}
}
