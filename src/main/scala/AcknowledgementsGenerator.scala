import java.io.File
import com.dd.plist._

object AcknowledgementsGenerator {

    def licenseFileToNSDictionary(license: File): NSDictionary = {
        val licenseDictionary = new NSDictionary()
        licenseDictionary.put("Type", new NSString("PSGroupSpecifier"))
        licenseDictionary.put("Title", new NSString(license.getName.split("\\.").head))
        val lines = scala.io.Source.fromFile(license.getAbsolutePath).mkString
        licenseDictionary.put("FooterText", new NSString(lines))
        licenseDictionary
    }

    def main(args: Array[String]) {

        if (args.length != 2)
            throw new Exception("Please specify a license input directory and an output path for the plist")

        val licenseFolder = new File(args(0))
        val outputFolder = new File(args(1))
        val licenseFiles = licenseFolder.listFiles().filter(_.isFile)
        val array = new NSArray(licenseFiles.length)
        licenseFiles.indices.foreach(index => array.setValue(index, licenseFileToNSDictionary(licenseFiles(index))))

        val root = new NSDictionary()
        root.put("StringsTable", new NSString("Acknowledgements"))
        root.put("Title", new NSString("Acknowledgements"))
        root.put("PreferenceSpecifiers", array)

        val outputFile = new File(outputFolder + "/Acknowledgements.plist")
        PropertyListParser.saveAsXML(root, outputFile)
    }

}


