package geotrellis.minicluster

import org.apache.accumulo.core.conf.Property;
import org.apache.accumulo.minicluster.impl.MiniAccumuloClusterImpl;
import org.apache.accumulo.minicluster.impl.MiniAccumuloConfigImpl;
import org.apache.hadoop.util.VersionInfo;
import org.apache.hadoop.util.VersionUtil;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.google.common.io.Files;
import mil.nga.giat.geowave.datastore.accumulo.minicluster.MiniAccumuloClusterFactory;

import scala.util.Try
import java.io.File
import java.util.concurrent.TimeUnit


object Main {
  def main(args: Array[String]) = {
    Logger.getRootLogger().setLevel(Level.INFO)

    val tempDir = Files.createTempDir()
    val zookeeperPort: Int = Try(sys.env("ZKPORT").toInt).getOrElse(2181)
    val instanceName = sys.env.getOrElse("INSTANCENAME", "AccumuloInstance")
    val user = sys.env.getOrElse("USER", "root")
    val password = sys.env.getOrElse("PASSWORD", "password")

    val miniAccumuloConfig = new MiniAccumuloConfigImpl(tempDir, password)
      .setNumTservers(2)
      .setInstanceName(instanceName)
      .setZooKeeperPort(zookeeperPort)

    val accumulo: MiniAccumuloClusterImpl = MiniAccumuloClusterFactory.newAccumuloCluster(
      miniAccumuloConfig,
      classOf[App]
    )

    accumulo.start()

    println("cluster running with instance name " + accumulo.getInstanceName() + " and zookeepers " + accumulo.getZooKeepers())

    Runtime.getRuntime().addShutdownHook(
      new Thread() {
        new Runnable {
          def run() = {
            try {
              accumulo.stop()
            }
            catch {
              case e: Exception => println("Error shutting down accumulo.")
            }
            println("Shutting down!")
          }
        }
      }
    )

    while (true) {
      Thread.sleep(TimeUnit.MILLISECONDS.convert(1L, TimeUnit.HOURS))
    }
  }
}
