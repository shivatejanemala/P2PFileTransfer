package peerProcessPackage.log;


import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import peerProcessPackage.configuration.PeerInfoDAO;


public class LoggerConfiguration {
    private static final String LOG_PROP_PATH = "/logger.properties";//"/edu/ufl/cise/cnt5106c/conf/logger.properties";
    private static final LoggerConfiguration _log = new LoggerConfiguration (Logger.getLogger("FileTransferProcess"));
    private final Logger _l;
    static {
        InputStream in = null;
        try{
            in = LoggerConfiguration.class.getResourceAsStream(LOG_PROP_PATH);
            if(in != null) {
            LogManager.getLogManager().readConfiguration(in);
                    }
            }
        catch (Exception e) {
            System.err.println("Error while reading the logger properties:" + e);
            System.exit(1);
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {}
            }
        }
    }



    private LoggerConfiguration (Logger log) {
        _l = log;
    }

    public static void configure(int peerId)
            throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Properties properties = new Properties();
        properties.load(LoggerConfiguration.class.getResourceAsStream(LOG_PROP_PATH));
        Handler handler = new FileHandler ("log_peer_" + peerId + ".log");
        Formatter formatter = (Formatter) Class.forName(properties.getProperty("java.util.logging.FileHandler.formatter")).newInstance();
        handler.setFormatter(formatter);
        handler.setLevel(Level.parse(properties.getProperty("java.util.logging.FileHandler.level")));
        _log._l.addHandler(handler);
    }

    public static LoggerConfiguration getLogger () {
        return _log;
    }

    public static String getPeerIdsAsString2 (Collection<Integer> peersIDs) {
        StringBuilder sb = new StringBuilder ("");
        boolean isFirst = true;
        for (Integer peerId : peersIDs) {
            if (isFirst) {
                isFirst = false;
            }
            else {
                sb.append(", ");
            }
            sb.append(peerId.intValue());
        }
        return sb.toString();
    }

    public static String getPeerIdsAsString (Collection<PeerInfoDAO> peers) {
        StringBuilder sb = new StringBuilder ("");
        boolean isFirst = true;
        for (PeerInfoDAO peer : peers) {
            if (isFirst) {
                isFirst = false;
            }
            else {
                sb.append(", ");
            }
            sb.append(peer.getPeerId());
        }
        return sb.toString();
    }

    public synchronized void conf (String msg) {
        _l.log(Level.CONFIG, msg);
    }

    public synchronized void debug (String msg) {
        _l.log(Level.FINE, msg);
    }

    public synchronized void info (String msg) {
        _l.log (Level.INFO, msg);
    }

    public synchronized void severe (String msg) {
        _l.log(Level.SEVERE, msg);
    }

    public synchronized void warning (String msg) {
        _l.log(Level.WARNING, msg);
    }

    public synchronized void severe (Throwable e) {
        _l.log(Level.SEVERE, stackTraceToString (e));
    }

    public synchronized void warning (Throwable e) {
        _l.log(Level.WARNING, stackTraceToString (e));
    }

    private static String stackTraceToString (Throwable t) {
        final Writer sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
