import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class that implements the channel used by wizards and miners to communicate.
 */
public class CommunicationChannel {
	/**
	 * Creates a {@code CommunicationChannel} object.
	 */

	ArrayBlockingQueue<Message> chann1 = new ArrayBlockingQueue<>(1000000);
	ArrayBlockingQueue<Message> chann2 = new ArrayBlockingQueue<>(1000000);
	ReentrantLock reentrantlock=new ReentrantLock();

	public CommunicationChannel() {

	}

	/**
	 * Puts a message on the miner channel (i.e., where miners write to and wizards
	 * read from).
	 * 
	 * @param message
	 *            message to be put on the channel
	 */
	public void putMessageMinerChannel(Message message) {
		try{
			chann2.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets a message from the miner channel (i.e., where miners write to and
	 * wizards read from).
	 * 
	 * @return message from the miner channel
	 */
	public Message getMessageMinerChannel() {
		Message msg2=null;
		try{
			msg2=chann2.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return msg2;
	}

	/**
	 * Puts a message on the wizard channel (i.e., where wizards write to and miners
	 * read from).
	 * 
	 * @param message
	 *            message to be put on the channel
	 */
	public void putMessageWizardChannel(Message message) {
		if(!reentrantlock.isHeldByCurrentThread())
			reentrantlock.lock();
		
		try{
				chann1.put(message);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}

		if(message.getData().equals("END")&&message.getCurrentRoom()==-1)
		{
			reentrantlock.unlock();
		}
	}

	/**
	 * Gets a message from the wizard channel (i.e., where wizards write to and
	 * miners read from).
	 * 
	 * @return message from the miner channel
	 */
	public Message getMessageWizardChannel() {
		Message msg1=null;
		try{
			msg1=chann1.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return msg1;
	}
}
