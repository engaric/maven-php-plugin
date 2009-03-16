package org.phpmaven.plugin.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Locale;
import java.util.Properties;

import org.apache.maven.reporting.MavenReportException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

/**
 * A maven 2.0 plugin for generating doxygen documentations.
 * This plugin is used in the <code>site</code> phase.
 * @author Christian Wiedemann
 * @goal doxygen
 * @phase site
 */

public class DoxygenReport extends AbstractApiDocReport {
	/**
	 * Path to the doxygen.exe. 
	 * If nothing is configured doxygen is expected in the path. 
	 * 
	 * @parameter expression="doxygen"
	 * @required
	 */
	private String doxyGenExe;


	/**
	 * The doxygen configuraton file. 
	 * By default it is searched under "src/site/doxygen"
	 * 
	 * @parameter expression="${basedir}/src/site/doxygen/doxygen.config";
	 * @required
	 */
	private File doxygenConfigFile;

	/**
	 * The doxygen file.
	 * 
	 * @parameter expression="${project.build.directory}/site/doxygen/doxygen.config";
	 * @required
	 * @readonly
	 */
	private File generatedDoxygenConfigFile;

	@Override
	protected void executeReport(Locale locale) throws MavenReportException {
		try {
			if (doxygenConfigFile.isFile()) {
				Properties properties = new Properties();
				properties.load(new FileInputStream(doxygenConfigFile));
				properties.put("INPUT",  getProject().getBasedir()+"/"+ getSourceDirectory());
				properties.put("OUTPUT_DIRECTORY", getApiDocOutputDirectory()
						.getAbsoluteFile().getPath()+"/doxygen");
				properties.put("PROJECT_NAME", getProject().getGroupId() + ":"
						+ getProject().getArtifactId());
				properties.put("PROJECT_NUMBER", getProject().getVersion());
				properties.put("STRIP_FROM_PATH",properties.get("INPUT"));
				properties.put("STRIP_FROM_INC_PATH",properties.get("INPUT"));


				writePropFile(properties, generatedDoxygenConfigFile,null);
				// properties.store(new FileWriter(generatedDoxygenConfigFile),
				// "");
				String execute = doxyGenExe+" \""
						+ generatedDoxygenConfigFile.getAbsolutePath() + "\"";
				getLog().debug("Doxygen execute: " + execute);
				Commandline commandLine = new Commandline(execute);

				CommandLineUtils.executeCommandLine(commandLine,
						new StreamConsumer() {
							public void consumeLine(String line) {
								getLog().debug(line);
							}
						}, new StreamConsumer() {
							public void consumeLine(String line) {
								getLog().debug(line);
							}
						});
				writeReport();
			} else {
				getLog().warn("No doxygen configuration file found. (search for: "+doxygenConfigFile.getAbsolutePath()+")");
			}
		} catch (Exception e) {
			throw new MavenReportException(e.getMessage(), e);
		}

	}

	private void writeReport() {

		getSink()
				.rawText(
						"<a href=\"doxygen/html/index.html\" target=\"_blank\">Show documention<br><iframe src=\"doxygen/html/index.html\" frameborder=0 style=\"border=0px;width:100%;height:400px\">");

	}


	
	public String getDescription(Locale locale) {
		return "Doxygen generated documentation";
	}

	public String getName(Locale locale) {
		return "doxygen";
	}

	public String getOutputName() {
		return "apidocs/doxygen";
	}

	@Override
	protected String getFolderName() {
		// TODO Auto-generated method stub
		return "doxygen";
	}


}