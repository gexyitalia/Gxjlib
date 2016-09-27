/*
 * Zerounozero Lab - Java Library
 * Library Version: 1.0.1
 * License:			Zerounozero Lab 2016 - all right reserved				
 */

package com.gexy.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gexy.cli.exception.CLIOptionArgumentRequiredException;
import com.gexy.cli.exception.CLIOptionArgumentValueRequiredException;

public class CliOpt{
	/**
	 * Questa classe implementa un gestore di opzioni(parser) a riga di comando
	 * per gestire gli argomenti passati ad una applicazione da Command 
	 * Line Interface(args)
	 * 
	 * Exeaple: simple use
	 * 	
	 * 	public static void main(String[] args){
	 * 		CLIOptionParser opts = new CLIOptionParser("gexyadmin",args);
	 *		
	 *		//Create the option definitions
	 *		opts.addOption('h', "host", "Host to connect", true, true);
	 *		opts.addOption('p', "port", "Port of the Host", true, true);
	 *		
	 * 		//start parser for data from command line
	 *		try {
	 *			opts.parse(args);
	 *		} catch (CLIOptionArgumentValueRequiredException | CLIOptionArgumentRequiredException e) {
	 *			System.err.println(e.getMessage());
	 *			opts.getHelp();
	 *			System.exit(1);
	 *		}
	 *	}
	 * 
	 * @author Zerounozero Lab - E. Liguori<mailto:info@zerounozerolab.com>
	 * @version 1.0.0
	 */

	private String[] cliArgs; //argomenti del metodo main
	private String programName; //nome del programma
	private List<CliOptStruct> opts; //opzioni
	private List<String> args; //Argomenti provenienti da cli
	private String afterTextHelp = "";
	private String beforeTextHelp = "";

	/**
	 * @param _args Arguments array passed from main method
	 */
	public  CliOpt(String _programName,String[] _args){
		cliArgs = _args;
		programName = _programName;
		opts = new ArrayList<CliOptStruct>(); 
	}

	/**
	 * 
	 * @param _args
	 */
	public CliOpt(String[] _args){
		cliArgs=_args;
		programName = "";
		opts = new ArrayList<CliOptStruct>(); 
	}


	/**
	 * Set string of program name used to generate help
	 * @param _value
	 */
	public void setProgramName(String _value){
		programName=_value;
	}

	/**
	 * Controlla la validità delle informazioni in args e per gli CLIOption, memorizza i valori degli argomenti
	 * e gestisce la correttezza delle informazioni, dopo questo passaggio gli argomenti sono pronti per 
	 * essere utilizzati dal programma
	 * @param _args
	 * @throws CLIOptionArgumentValueRequiredException
	 * @throws CLIOptionArgumentRequiredException 
	 */
	public boolean parse() throws CLIOptionArgumentValueRequiredException, CLIOptionArgumentRequiredException{
		//converto la lista in un array per lavorare direttamente sugli oggetti
		CliOptStruct[] tmp = opts.toArray(new CliOptStruct[opts.size()]); 

		/*
		 * creo una copia dell'array args, le opzioni e relativi 
		 * valori verranno eliminati e alla fine verrà creato un array per gli
		 * argomenti che rimangono, in questo modo se oltre alle opzioni vengono 
		 * passati anche argomenti da usare come dati, questi possono essere 
		 * utilizzati.
		 */
		args = new ArrayList<String>(Arrays.asList(cliArgs));


		//ciclo sull'array CliOptStruct
		for(int a=0;a<tmp.length;a++){
			//ciclo sugli argomenti
			for(int b=0;b<this.cliArgs.length;b++){
				//se è presente l'argomento corto nella command line
				if(tmp[a].hasShortArgument() && (this.cliArgs[b].equals("-"+tmp[a].getArgument()))){
					tmp[a].setPassed(); //setto argomento come passato
					args.remove(b); 
					//controllo se è richiesto un valore per l'argomenti cli
					if(tmp[a].isRequiredValue()){
						//controllo se ci sono dati nella posizione successiva
						if((b+1)<this.cliArgs.length){
							//se il prossimo argomento non è ne lungo ne corto allora è il valore da assegnare
							if(!this.cliArgs[b+1].substring(0, 1).equals("-")&&!this.cliArgs[b].substring(0, 2).equals("--")){
								//il dato nella prossima posizione viene memorizzato come valore dell'argomento
								tmp[a].setValue(this.cliArgs[b+1]);
//								args.remove(b); //essendo stato tolto alla posizione b sopra ora questo non sarà b+1 ma b
							}else{
								//errore l'argomento richiede un valore
								throw new CLIOptionArgumentValueRequiredException("Command line argument "+this.cliArgs[b]+" require value, but next value is an argument");
							}
						}else{
							//errore non c'è niente dopo questo argomento
							throw new CLIOptionArgumentValueRequiredException("Command line argument "+this.cliArgs[b]+" require value, but aruments are finished");
						}
					}
					//se è presente l'argomento lungo nella command line
					else if(tmp[a].hasLongArgument() && (this.cliArgs[b].equals("--"+tmp[a].getLongArgument()))){
						tmp[a].setPassed(); //setto argomento come passato
						args.remove(b);
						//controllo se è richiesto un valore per l'argomenti cli
						if(tmp[a].isRequiredValue()){
							//controllo se ci sono dati nella posizione successiva
							if((b+1)<this.cliArgs.length){
								//se il prossimo argomento non è ne lungo ne corto allora è il valore da assegnare
								if(!this.cliArgs[b+1].substring(0, 1).equals("-")&&!this.cliArgs[b].substring(0, 2).equals("--")){
									//il dato nella prossima posizione viene memorizzato come valore dell'argomento
									tmp[a].setValue(this.cliArgs[b+1]);
									args.remove(b);
								}else{
									//errore l'argomento richiede un valore
									throw new CLIOptionArgumentValueRequiredException("Command line argument "+this.cliArgs[b]+" require value, but next value is an argument");
								}
							}else{
								//errore non c'è niente dopo questo argomento
								throw new CLIOptionArgumentValueRequiredException("Command line argument "+this.cliArgs[b]+" require value, but aruments are finished");
							}
						}
					}
				}
			}
		}

		//ciclo sull'array CliOptStruct
		for(int a=0;a<tmp.length;a++){
			//se l'argomento è richiesto e non è stato passato
			if(tmp[a].isRequired()){
				if(!tmp[a].isPassed()){
					//compongo il messaggio
					String msg="Command line argument ";
					if(tmp[a].hasShortArgument()){msg+="-"+tmp[a].getArgument()+" or ";}
					if(tmp[a].hasLongArgument()){msg+="--"+tmp[a].getLongArgument()+" ";}
					msg+="is required";
					throw new CLIOptionArgumentRequiredException(msg);
				}
			}

		}

		opts.clear(); //pulisco la List
		for(int c=0;c<tmp.length;c++){opts.add(tmp[c]);} //popolo la List con gli oggetti modificati
		tmp=null; //cancello l'array

		return true;
	}

	/**
	 * Add an short and long option(CliOptStruct class)
	 * 
	 * @param _argShort	The short argument ex. -t(without -)
	 * @param _argLong The long argument ex. --word(without --)
	 * @param _description The description of argument for help text generator
	 * @param _argReq If set true, this argument is required from command line
	 * @param _valReq If set true, a value for de argument is required from command line
	 * @see	CliOptStruct
	 */
	public void addOption(char _argShort, String _argLong, String _description, boolean _argReq, boolean _valReq) {
		opts.add(new CliOptStruct(_argShort, _argLong, _description, _argReq, _valReq));
	}
	/**
	 * Add an short and long option(CliOptStruct class)
	 * 
	 * @param _argLong
	 * @param _description
	 * @param _argReq
	 * @param _valReq
	 * @see	CliOptStruct
	 */
	public void addOption(String _argLong, String _description, boolean _argReq, boolean _valReq) {
		CliOptStruct opt = new CliOptStruct(' ', _argLong, _description, _argReq, _valReq);
		opts.add(opt);
	}
	/**
	 * Add an short and long option(CliOptStruct class)
	 * 
	 * @param _argShort
	 * @param _description
	 * @param _argReq
	 * @param _valReq
	 * @see	CliOptStruct
	 */
	public void addOption(char _argShort, String _description, boolean _argReq, boolean _valReq) {
		CliOptStruct opt = new CliOptStruct(_argShort,  " ", _description, _argReq, _valReq);
		opts.add(opt);
	}

	/**
	 * Return CliOptStruct object for argument passed by method argument
	 * @param	String	Long or short argument to extract
	 * @throws CLIOptionArgumentNotSetException 
	 * @return	CliOptStruct
	 */
	public CliOptStruct getOption(String _arg){
		boolean shortArg;

		if(_arg.length() == 1){shortArg=true;}else{shortArg=false;}
		for(int a=0;a<opts.size();a++){
			if(shortArg){
				if(opts.get(a).getArgument() == _arg.charAt(0)){
					return opts.get(a);
				}
			}else{
				if(opts.get(a).getLongArgument().equals(_arg)){
					return opts.get(a);
				}
			}
		}
		return null;
	}

	/**
	 * Return an array contain arguments remained from initial cli
	 * arguments without parsed options
	 * @return String[]
	 */
	public String[] getArguments(){
		return args.toArray(new String[args.size()]);
	}

	/**
	 * Return true if the long argument is passed from command line
	 * 
	 * @param _opt	Character of the long argument ex. --word, -version, etc.
	 * @return		true if argument is passed, false if not
	 * @throws CLIOptionArgumentNotSetException 
	 */
	public boolean isPassed(String _opt) {
		if(getOption(_opt)!=null){
			if(getOption(_opt).isPassed()){return true;}else{return false;}
		}else{
			//vuol dire che non è stato definito l'argomento richieso
			return false;
		}
	}



	/**
	 * Return the description for an long argument spcified by _opt
	 * 
	 * @param _opt Character of the argument(without --)
	 * @return String of description for the argument set with add()
	 */
	public String getDescription(String _opt){
		return getOption(_opt).getDescription();
	}

	/**
	 * Return the value of an long argument specified by _opt
	 * 
	 * @param _opt 	String of the argument(without --)
	 * @return 		String of value for the argument, if not set return null
	 */
	public String getValue(String _opt){
		return getOption(_opt).getValue();
	}

	/**
	 * Add own text after auto-generated help text
	 * @param _text
	 */
	public void addTextAfterHelp(String _text){
		this.afterTextHelp=_text;
	}

	/**
	 * Add own text before auto-generated help text
	 * @param _text
	 */
	public void addTextBeforeHelp(String _text){
		this.beforeTextHelp=_text;
	}

	/**
	 * Return the descriptions, and argument(long and short) and usage string
	 * 
	 * @return String with list of argument and description and string of usage
	 */
	public String getHelp(){
		String out="";

		out+=this.beforeTextHelp;

		//uso
		out += "\nUsage: "+this.programName+" ";
		//short arg
		out += "[";
		int i = 0;
		while(i<opts.size()){
			out += "-"+opts.get(i).getArgument();
			if(i<opts.size()-1){out+="|";}
			i++;
		}
		out += "] ";

		//long arg
		out += "[";
		i = 0;
		while(i<opts.size()){
			out += "--"+opts.get(i).getLongArgument();
			if(i<opts.size()-1){out+="|";}
			i++;
		}
		out += "]\n\n";

		out += "*\tArgument required\n";
		out += "**\tValue required\n\n";

		//lista degli argomenti
		i = 0;
		while(i<opts.size()){
			out += "-"+opts.get(i).getArgument()+"\t"; //short arg
			if(opts.get(i).getLongArgument().equals(null)){out += "\t\t";}else{out += "--"+opts.get(i).getLongArgument()+"\t";} //long arg
			out += opts.get(i).getDescription()+"\n";//description
			i++;
		}
		out +="\n";	

		out+=this.afterTextHelp;

		out +="\n";
		return out;
	}

	/**
	 * Retun an array of String that contain data from cli without argument and it's values
	 * @return String[]
	 */
	public String[] getNoArguments(){
		return args.toArray(new String[args.size()]);
	}
}