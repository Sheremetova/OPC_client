import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.*;
import org.openscada.opc.lib.da.*;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

public class Test {
  public static void main(String[] args) throws AlreadyConnectedException, UnknownHostException,
  NotConnectedException, DuplicateGroupException, AddFailedException, InterruptedException {
    // create connection information
    final ConnectionInformation ci = new ConnectionInformation();
    ci.setHost("127.0.0.1");
    ci.setDomain("elephantik");
    ci.setUser("el");
    ci.setPassword("mouse058842");
    ci.setProgId("Graybox.Simulator.1");

    // ci.setClsid("2C2E36B7-FE45-4A29-BF89-9BFBA6A40857"); // if ProgId is not working,
    // try it using the Clsid instead
    final String itemId = "numeric.saw.float";
    // create a new server
    final Server server = new Server(ci, Executors.newSingleThreadScheduledExecutor());

    try {
      // connect to server
      server.connect();
      // add sync access, poll every 500 ms
      final AccessBase access = new SyncAccess(server, 500);
      access.addItem(itemId, new DataCallback() {
        @Override
        public void changed(Item item, ItemState state) {
          System.out.println(state);
        }
      });
      // start reading
      access.bind();
      // wait a little bit
      Thread.sleep(10 * 1000);
      // stop reading
      access.unbind();
    } catch (final JIException e) {
      System.out.println(String.format("%08X: %s", e.getErrorCode(), server.getErrorMessage(e.getErrorCode())));
    }
  }
}
