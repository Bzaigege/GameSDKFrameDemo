package com.bzai.gamesdk.build.tools.exec;

import com.bzai.gamesdk.build.utils.Utils;

import java.io.*;
import java.util.List;

public class Shell {

    public static void execute(List<String> commandSet, String logOutput) throws Exception{

        Process ps = null;
        int exitValue = -99;
        ProcessBuilder builder = new ProcessBuilder(commandSet);
        ps = builder.start();

        OutputStreamWriter outputStreamWriter = null;
        if (!Utils.isEmpty(logOutput)) {
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(logOutput, true));
            StreamForwarder errorStream = new StreamForwarder(ps.getErrorStream(), outputStreamWriter, 1, "ERROR");
            StreamForwarder outputStream = new StreamForwarder(ps.getInputStream(), outputStreamWriter, 1, "OUTPUT");
            errorStream.start();
            outputStream.start();
        }else{
            outputStreamWriter = new OutputStreamWriter(System.out);
            StreamForwarder errorStream = new StreamForwarder(ps.getErrorStream(), outputStreamWriter, 2, "ERROR");
            StreamForwarder outputStream = new StreamForwarder(ps.getInputStream(), outputStreamWriter, 2, "OUTPUT");
            errorStream.start();
            outputStream.start();
        }

        exitValue = ps.waitFor();
        ps.destroy();
        if (exitValue != 0){
            String inputCommandLine = "";
            for (String option:commandSet){
                if (!Utils.isEmpty(inputCommandLine)){
                    inputCommandLine = inputCommandLine + " ";
                }
                inputCommandLine = inputCommandLine + option;
            }
            throw new Exception("Could not exec command line ["+inputCommandLine+"]");
        }
    }


    static class StreamForwarder extends Thread{

        private final InputStream mIn;
        private final String mType;
        private int mStreamType;
        private final OutputStreamWriter mOs;

        StreamForwarder(InputStream is, OutputStreamWriter os, int streamType, String type) {
            mIn = is;
            mType = type;
            mOs = os;
            mStreamType = streamType;
        }

        @Override
        public void run() {
            BufferedWriter bw = null;
            BufferedReader br = null;

            try {
                bw = new BufferedWriter(mOs);
                br = new BufferedReader(new InputStreamReader(mIn));
                String line;
                while ((line = br.readLine()) != null) {
                    if (mType.equals("OUTPUT")) {
                        bw.write(line+"\n");
                    } else {
                        bw.write(line+"\n");
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }finally {
                try{
                    //使用的是文件流,需要将流关闭。
                    if (1 == mStreamType){
                        if (br != null) {
                            br.close();
                        }
                        if (null != bw){
                            bw.close();
                        }
                    }
                }catch (IOException e){
                }
            }
        }
    }
}
