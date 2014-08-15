package tf.RfidDemo;

public class SerialPort {
	static {
		try {
			System.load("/system/lib/libtf_serialport_RfidDemo.so");
		} catch (UnsatisfiedLinkError e) {
			// TODO: handle exception
		}
	}

	public native static int open(String dev);

	public native static int close(final int fd);

	public native static int writeData(final int fd, byte[] str);

	public native static String receiveData(final int fd);
}