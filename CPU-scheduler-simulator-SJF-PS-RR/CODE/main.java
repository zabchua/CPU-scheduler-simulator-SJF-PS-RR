package CODE;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.time.Duration;

public class main
{
    static Scanner read = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException{
        String algo;
    
        int thisMenu = 0;
        String thisOption = "";
        int validOption = 0;
        Scanner thisChoice = new Scanner(System.in);
    
        while (thisMenu==0) {
            
            algo = chooseAlgorithm();
    
            if(algo.equals("A")){
                SJF(inputData(algo));
            }else if(algo.equals("B")){
                PS(inputData(algo));
            }else if(algo.equals("C")){
                RR(inputData(algo));
            }
    
            System.out.println("========================================================================");
            System.out.println();
            System.out.println("Choose an option:");
            System.out.println("A. Restart");
            System.out.println("B. Terminate");
            
            
    
            while (validOption==0) {
                
                System.out.println();
                System.out.println("Enter Choice (A/B):");
                thisOption = thisChoice.nextLine();
                
                if (thisOption.equals("B")|| thisOption.equals("b") || thisOption.equals("B.") || thisOption.equals("b.")) {
                    System.out.println();
                    System.out.println("Program terminated, closing window...");
                    System.out.println();
                    thisMenu = 1;
                    validOption=1;
                    Thread.sleep(2000);
                }
                else if (thisOption.equals("A")|| thisOption.equals("a") || thisOption.equals("A.") || thisOption.equals("a.")){
                    validOption=1;
                }
                else{
                    System.out.println();
                    System.out.println("Error: Invalid input");
                    System.out.println();
                    thisChoice = new Scanner(System.in);
                }
            }
    
            validOption=0;
            
    
        }
    }

    // SHORTEST JOB FIRST
    public static void SJF(String[][] data) throws InterruptedException{
        int sec = 0;
        int isFirstIter = 1;
        int waitFirst, burstTime, current;

        final String[][] origData = new String[data.length][4];
        for(int i=0; i<data.length; i++){
            for(int j=0; j<data[0].length; j++){
                origData[i][j] = data[i][j];
            }
        }

        LocalDateTime startEverything = LocalDateTime.now();
        LocalDateTime arrived;
        LocalDateTime[] startTimes = new LocalDateTime[data.length];

        System.out.println("Shortest Job First Algorithm");

        current = nextProcessForSJF(data, isFirstIter, sec);

        String timeStamp;
        String[] startTime = new String[data.length];
        String[] endTime = new String[data.length];

        Timer timer = new Timer();

        for(int y=0; y<data.length; y++){
            
            timeStamp = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());
            if(y==0){startEverything = LocalDateTime.now();}
            startTime[current] = timeStamp;
            startTimes[current] = LocalDateTime.now();
            waitFirst = Integer.parseInt(data[current][2]);

            while(waitFirst>sec){
                timeStamp = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());
                System.out.println(timeStamp + "\tIdle... ");
                sec++;
                Thread.sleep(1000);
            }
            burstTime = Integer.parseInt(data[current][1]);
            while(burstTime>0){
                timeStamp = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());
                System.out.println(timeStamp + "\tRunning Process " + data[current][0] + "...");
                burstTime--;
                sec++;
                Thread.sleep(1000);
            }
            isFirstIter = 0;
            data[current][2] = "-1";
            endTime[current] = timeStamp;
            current = nextProcessForSJF(data, isFirstIter, sec);
        }
        timer.cancel();

        double totalWait = 0;
        double totalTurn = 0;
        double avgWait = 0;
        double avgTurn = 0;
        long wait;

        //PRINTING OF SUMMARY
        System.out.println("\nSummary");
        System.out.println("\t\tTime Duration \t\t\t Waiting Time \t\t Turnaround Time");
        
        for(int j=0; j<data.length; j++){
            arrived = startEverything.plusSeconds(Integer.parseInt(origData[j][2]));

            Duration duration = Duration.between(arrived, startTimes[j]);
            if(duration.getSeconds()<0){wait = 0;}else{wait = duration.getSeconds();}
            long TAT = duration.getSeconds() + Long.parseLong(origData[j][1]);

            totalWait = totalWait + duration.getSeconds();
            totalTurn = totalTurn + TAT;

            System.out.println(data[j][0] + "\t\t" + startTime[j] + "-" + endTime[j] + "\t\t " + wait + "\t\t\t " + TAT);
        }

        avgWait = totalWait/data.length;
        avgTurn = totalTurn/data.length;

        System.out.println("\nAverage Waiting Time: " + avgWait + "seconds");
        System.out.println("Average Turnaround Time: " + avgTurn + "seconds\n");
    }

    public static int nextProcessForSJF(String[][] data, int isFirstIter, int sec) {
        int number, burst;
        int lowest = -1;
        int lowestBurst = -1;
        int next = -1; //

        // FOR THE FIRST PROCESS
        if(sec==0){
        for(int x=0; x<data.length; x++){
            number = Integer.parseInt(data[x][2]);
            burst = Integer.parseInt(data[x][1]);
            if((lowest==-1)&&(number!=-1)){
                lowest = number;
                lowestBurst = burst;
                next = x;
            }
            else if((number<lowest)&&(number!=-1)){
                lowest = number;
                lowestBurst = burst;
                next = x;
            }
            else if((number==lowest)&&(number!=-1)){
                if(burst<lowestBurst){
                    lowestBurst = burst;
                    next = x;
                }
            }
        }}
        else{
        for(int x=0; x<data.length; x++){
            number = Integer.parseInt(data[x][2]);
            burst = Integer.parseInt(data[x][1]);

            if(number>sec){continue;}

            if((lowestBurst==-1)&&(number!=-1)){
                lowest = number;
                lowestBurst = burst;
                next = x;
            }
            else if((burst<lowestBurst)&&(number!=-1)){
                lowest = number;
                lowestBurst = burst;
                next = x;
            }
            else if((burst==lowestBurst)&&(number!=-1)){
                if(number<lowest){
                    lowest = number;
                    lowestBurst = burst;
                    next = x;
                }
            }
        }}

        return next;
    }

    // PRIORITY SCHEDULING
    public static void PS(String[][] data) throws InterruptedException{
        int sec = 0;
        int isFirstIter = 1;
        int waitFirst, burstTime, current;

        final String[][] origData = new String[data.length][4];
        for(int i=0; i<data.length; i++){
            for(int j=0; j<data[0].length; j++){
                origData[i][j] = data[i][j];
            }
        }

        LocalDateTime startEverything = LocalDateTime.now();
        LocalDateTime arrived;
        LocalDateTime[] startTimes = new LocalDateTime[data.length];

        System.out.println("Priority Scheduling Algorithm");

        current = nextProcessForPS(data, isFirstIter, sec);

        String timeStamp;
        String[] startTime = new String[data.length];
        String[] endTime = new String[data.length];

        Timer timer = new Timer();

        for(int y=0; y<data.length; y++){
            
            timeStamp = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());
            if(y==0){startEverything = LocalDateTime.now();}
            startTime[current] = timeStamp;
            startTimes[current] = LocalDateTime.now();
            waitFirst = Integer.parseInt(data[current][2]);
            
            while(waitFirst>sec){
                timeStamp = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());
                System.out.println(timeStamp + "\tIdle... ");
                sec++;
                Thread.sleep(1000);
            }
            burstTime = Integer.parseInt(data[current][1]);
            while(burstTime>0){
                timeStamp = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());
                System.out.println(timeStamp + "\tRunning Process " + data[current][0] + "...");
                burstTime--;
                sec++;
                Thread.sleep(1000);
            }
            isFirstIter = 0;
            data[current][2] = "-1";
            endTime[current] = timeStamp;
            current = nextProcessForPS(data, isFirstIter, sec);
        }
        timer.cancel();

        double totalWait = 0;
        double totalTurn = 0;
        double avgWait = 0;
        double avgTurn = 0;
        long wait;

        //PRINTING OF SUMMARY
        System.out.println("\nSummary");
        System.out.println("\t\tTime Duration \t\t\t Waiting Time \t\t Turnaround Time");
        
        for(int j=0; j<data.length; j++){
            arrived = startEverything.plusSeconds(Integer.parseInt(origData[j][2]));

            Duration duration = Duration.between(arrived, startTimes[j]);
            if(duration.getSeconds()<0){wait = 0;}else{wait = duration.getSeconds();}
            long TAT = duration.getSeconds() + Long.parseLong(origData[j][1]);

            totalWait = totalWait + duration.getSeconds();
            totalTurn = totalTurn + TAT;

            System.out.println(data[j][0] + "\t\t" + startTime[j] + "-" + endTime[j] + "\t\t " + wait + "\t\t\t " + TAT);
        }

        avgWait = totalWait/data.length;
        avgTurn = totalTurn/data.length;

        System.out.println("\nAverage Waiting Time: " + avgWait + " seconds");
        System.out.println("Average Turnaround Time: " + avgTurn + " seconds\n");
    }

    public static int nextProcessForPS(String[][] data, int isFirstIter, int sec){
        int number, priority;
        int lowest = -1;
        int highestpriority = -1;
        int next = -1; //

        if(sec==0){
        for(int x=0; x<data.length; x++){
            number = Integer.parseInt(data[x][2]);
            priority = Integer.parseInt(data[x][3]);
            
            if((lowest==-1)&&(number!=-1)){
                lowest = number;
                highestpriority = priority;
                next = x;
            }
            else if((number<lowest)&&(number!=-1)){
                lowest = number;
                highestpriority = priority;
                next = x;
            }
            else if((number==lowest)&&(number!=-1)){
                if(priority<highestpriority){
                    highestpriority = priority;
                    next = x;
                }
            }
        }}
        else{
        for(int x=0; x<data.length; x++){
            number = Integer.parseInt(data[x][2]);
            priority = Integer.parseInt(data[x][3]);

            if(number>sec){continue;}

            if((highestpriority==-1)&&(number!=-1)){
                lowest = number;
                highestpriority = priority;
                next = x;
            }
            else if((priority<highestpriority)&&(number!=-1)){
                lowest = number;
                highestpriority = priority;
                next = x;
            }
            else if((priority==highestpriority)&&(number!=-1)){
                if(number<lowest){
                    lowest = number;
                    highestpriority = priority;
                    next = x;
                }
            }
        }}

        return next;
    }

    //INPUT DATAAA
    public static String[][] inputData(String algo){
        int lines = 0;
        int words = 0;
        
        int wpl, numberOfProcesses;

        boolean okayFile = false;
        String[][] ha = new String[3][3];

        String fileName = "";

        int required = 6;
        if(algo.equals("A")||algo.equals("C")){required=5;}
    
        while(okayFile==false){
            int rows = 0;
            int columns = 0;
            int headerTitles = 0;
            String error = "No errors found, information processing successful";
            int skip = 0;
            boolean exists = false;

            while(exists==false){
                System.out.println("\nEnter file name with extension (i.e. Test.txt)");
                fileName = read.nextLine();

                File f = new File(fileName);
                exists = f.exists();

                if(exists){
                    System.out.println("\nQueuing Processes...");}
                else{
                    String decision = "-1";
                    while(!decision.equals("1")){
                        System.out.println("\nThe file doesn't exist. Want to try and enter a valid file? 1/0:");

                        decision = read.nextLine();

                        if(decision.equals("0")){
                            System.out.println("\nProgram Ended.");
                            System.exit(0);
                        }else if(!decision.equals("1")){
                            System.out.println("\nKindly enter a value of 1 or 0:");
                        }
                    }
                }
            }

            try{
                Scanner s = new Scanner( new File(fileName) );

                while( s.hasNextLine() ){
                    columns = 0;
                    rows++;
                    Scanner s2 = new Scanner(s.nextLine());

                    while (s2.hasNext()) {
                        String s3 = s2.next();
                        columns++;

                        //Check if the header is sunod-sunod
                        if(rows==1){
                            if(columns==1){
                                if(s3.equals("Process")||s3.equals("process")){
                                    headerTitles++;
                                }
                            }else if(columns==2){
                                if(s3.equals("Burst")||s3.equals("burst")){
                                    headerTitles++;
                                }
                            }else if(columns==3){
                                if(s3.equals("Time")||s3.equals("time")){
                                    headerTitles++;
                                }
                            }else if(columns==4){
                                if(s3.equals("Arrival")||s3.equals("arrival")){
                                    headerTitles++;
                                }
                            }else if(columns==5){
                                if(s3.equals("Time")||s3.equals("time")){
                                    headerTitles++;
                                }
                            }else if(columns==6){
                                if(algo.equals("B")&&(s3.equals("Priority")||s3.equals("priority"))){
                                    headerTitles++;
                                }
                            }
                        }
                        
                        //Check if the values after the first column is an integer
                        if(rows>1&&columns>1){
                            if(!check(s3)){
                                skip = 1;
                                error = "Invalid input (must be an integer greater than or equal to 0)";
                                break;
                            }
                            //Check if the burst time is equal to 0
                            if(rows>1&&columns==2&&!checkburst(s3)){
                                skip = 1;
                                error = "Burst time must not be 0";
                            }
                        }
                    }
                    s2.close();
                }

                // Check header
                if(headerTitles<required){
                    error = "Header error";
                }
                //Check if it is at least 3 columns
                if(columns<required-2){
                    error = "Incorrect number of data, must be at least " + (required-2);
                }
                // Check if there is at least 2 rows
                if(rows<2){
                    error = "Rows must be at least 2, one header and at least one process";
                }

                System.out.println("\nError:" + error);
                if(error.equals("No errors found, information processing successful")){
                    okayFile=true;
                }

                s.close();
            }

            catch(IOException e){
                System.out.println( e );
            }
        }

        try{   
            Scanner s = new Scanner( new File(fileName) );
            while( s.hasNextLine() ){
                //System.out.println( s.nextLine() );
                lines++;
                Scanner s2 = new Scanner(s.nextLine());
                while (s2.hasNext()) {
                    String s3 = s2.next();
                    //System.out.println(s3);
                    words++;
                }
                s2.close();
            }
            s.close();

            wpl = words/lines;
            numberOfProcesses = lines-1;
            //System.out.println(wpl);

            String[][] data = new String[numberOfProcesses][wpl];

            Scanner s3 = new Scanner( new File("Test.txt") );

            for(int x=0; x<numberOfProcesses; x++){
                
                if(x==0){s3.nextLine();}
                Scanner s4 = new Scanner(s3.nextLine());

                for(int y=0; y<wpl; y++){
                    data[x][y] = s4.next();
                }
                s4.close();
            }
            s3.close();

            //System.out.println(Arrays.deepToString(data));
            return data;
        }

        catch(IOException e){
            System.out.println( e );
        }
        return ha;
    }

    public static boolean check(String string){
        boolean answer;
        int intValue;

        try {
            intValue = Integer.parseInt(string);
            if(intValue>=0){
                answer = true;
            }else{
                answer = false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Input String cannot be parsed to Integer.");
            answer = false; 
        }

        return answer;
    }

    public static boolean checkburst(String string){
        boolean answer;
        int intValue;

        try {
            intValue = Integer.parseInt(string);
            if(intValue>0){
                answer = true;
            }else{
                answer = false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Burst must not be 0");
            answer = false; 
        }

        return answer;
    }

    public static void RR(String[][] data) throws InterruptedException{


        Timer timer = new Timer();
    
        int num;
        double avgWT = 0;
        double avgTaT = 0;
    
        Scanner tq = new Scanner(System.in);
    
        int validLoop = 0;
        int timeQuantum = 0;
    
        System.out.print("Provide time quantum in seconds: ");
        while (validLoop==0) {
            if (tq.hasNextInt()) {
                timeQuantum = tq.nextInt();
                validLoop=1;       
            }
            else{
                System.out.println("Error: Invalid input, try again");
                System.out.println();
                tq = new Scanner(System.in);
                System.out.print("Provide time quantum in seconds: ");
                timeQuantum=1;
            }
            if (timeQuantum <= 0) {
                System.out.println("Error: Time quantum must be greater than 0");
                System.out.println();
                tq = new Scanner(System.in);
                System.out.print("Provide time quantum in seconds: ");
                validLoop=0;
            } 
        }
        
        System.out.println();
        System.out.println("Round Robin Algorithm");
    
    
        String[] timeMarkers = new String[100];
        for(int j = 0; j<100; j++){
            timeMarkers[j]=String.valueOf(-1);
        }
        int marker=0;
    
        int qcounter = 0;
        int pcounter = 0;
        int numberOfProcesses = data.length;
        int myTurn = 0;
        int timerCounter = 0;
        int temp;
    
        int mostDur = 0;
        int lesserDur = 0;
    
        int[] burstTime = new int[numberOfProcesses];
        for(int x = 0; x<numberOfProcesses ; x++){
            burstTime[x] = Integer.parseInt(data[x][1]);
        }
        int[] completionTime= new int[numberOfProcesses*2];
        int ctHolder=0;
    
        String timeStamp;
    
        int myCheck=0;
    
        int myFit=0;
        int myAnti=0;
        int myRepos=0;
        int tempCounter=0;
        int temper=0;
    
        int myNonOut=0;
    
        int hasRan = 0;
    
    
    
    
        int myNewSpace = numberOfProcesses + numberOfProcesses;
    
        int[] processesToRun = new int[myNewSpace];
    
    
        for (int i = 0; i <= numberOfProcesses; i++) {
            processesToRun[i]=-2;
        }
    
        while(pcounter < numberOfProcesses){
    
    
            if (qcounter==timeQuantum) {
                qcounter = 0;     
                processesToRun[0]=-2;
            }
    
            // System.out.println("1st queue " + processesToRun[0] + " " + processesToRun[1] + " " + processesToRun[2] + " " + processesToRun[3] + " this qcounter: " + qcounter);
    
    
    
            while (qcounter < timeQuantum) {
                
    
                for (int i = 0; i <= numberOfProcesses; i++) {       // to see if the queue is empty (no wait yet, not even idle)
                    if (processesToRun[i]!= -2) {
                        myAnti = 1;
                    }
                }
    
    
                if (myAnti == 1) {
                    for (int k = 0; k <= numberOfProcesses; k++) {               // to sort order in queue
                        if (processesToRun[k] == -2) {
                            for(int m = k; m < numberOfProcesses; m++){
                                if(m < numberOfProcesses){
                                    processesToRun[m]=processesToRun[m+1];
                                    processesToRun[m+1]=-2;
                                }
                                else{
                                    processesToRun[m]=-2;
                                }
                            }
                            
                        }
                    }
                }
    
                // System.out.println("2nd queue " + processesToRun[0] + " " + processesToRun[1] + " " + processesToRun[2] + " " + processesToRun[3] + " this qcounter: " + qcounter);
                
                myAnti = 0;
    
                while (myFit == 0) {
                    
                    for (int i = 0; i < numberOfProcesses; i++) {               // to list queue
                        num = Integer.parseInt(data[i][2]);
                        temper = tempCounter;
                        
                        if (num <= timerCounter){
                            if (processesToRun[tempCounter]==-2) {
                                if (Integer.parseInt(data[i][1])!=0) {
                                    for(int g = 0 ; g <= numberOfProcesses; g++){
                                        if (processesToRun[g] == i) {
                                            myTurn = 1;
                                            // System.out.println("exist1");
                                        }
                                    }
                                    if(myTurn == 0){
                                        
                                        processesToRun[tempCounter]=i;
                                        tempCounter = tempCounter + 1;
                                    }
                                    else{
                                        processesToRun[tempCounter]=-2;
                                    }
                                    myTurn = 0;
    
                                }
    
                            }
                            else{
                                
                                while(myCheck == 0){
                                    if (Integer.parseInt(data[i][1])!=0) {
                                        for(int g = 0 ; g <= numberOfProcesses; g++){
                                            if (processesToRun[g] == i) {
                                                myTurn = 1;
                                                // System.out.println("exist2");
                                            }
                                        }
                                        if(myTurn == 0){
    
                                            while(myNonOut==0){
                                            
                                                if (temper <= numberOfProcesses) {
                                                    temper = temper + 1;
                                                    if (processesToRun[temper]==-2) {
                                                        processesToRun[temper]=i;
                                                        tempCounter = tempCounter + 1;
                                                        myNonOut = 1;
                                                        // System.out.println("have space");
                                                    }               
                                                }
                                            }
                                            myNonOut=0;
                                            temper = 0;
                                            myCheck = 1;
                                        }
                                        else{
                                            // System.out.println("no space");
                                            myCheck = 1;
                                        }
    
                                    }
                                    else{
                                        myCheck = 1;
                                    }
     
                                }
                                myCheck = 0;
                            }
                            
                        }
                        else{
                            // System.out.println("my empty???");
                            myRepos = myRepos + 1;
                            if (myRepos == numberOfProcesses) {
                                processesToRun[tempCounter]=-1;
                                tempCounter = numberOfProcesses+1;
                            }
                            
                        }
    
                        myTurn=0;
                            
                    }
                    myCheck = 0;
                    myFit = 1;
                    myRepos = 0;
                    
                }
                
                myFit = 0;
                tempCounter = 0;
    
                //System.out.println("3rd queue " + processesToRun[0] + " " + processesToRun[1] + " " + processesToRun[2] + " " + processesToRun[3] + " this qcounter: " + qcounter);
    
                
    
                timeStamp = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());
    
                if (processesToRun[0]==-1) {
    
                    System.out.println(timeStamp + "\tIdle... ");
                    hasRan=1;
                    qcounter = timeQuantum;
                    timerCounter++;
                    Thread.sleep(1000);
                    
    
                }
                else if (processesToRun[0]==-2) {
                    for(int x = 0; x < numberOfProcesses; x++){
                        if(burstTime[x]==0){
                            timeMarkers[marker]=String.valueOf(x);
                            marker=marker+1;
                            timeMarkers[marker]="No Burst Time";
                            marker=marker+1;
                            timeMarkers[marker]="No Duration";
                            marker=marker+1;
                            pcounter++;
                            completionTime[ctHolder]= x;
                            ctHolder=ctHolder + 1;
                            completionTime[ctHolder]=0;
                            ctHolder=ctHolder + 1;
                            qcounter = timeQuantum;
                        }
                    }
                    if(hasRan==1){
                        System.out.println(timeStamp + "\tIdle... ");
                        qcounter = timeQuantum;
                        timerCounter++;
                        Thread.sleep(1000);
                    }
                    
                }
                else{
                    hasRan=1;
                    if (Integer.parseInt(data[processesToRun[0]][1]) == 0) {
                        qcounter = timeQuantum;
                    }
                    else{
                        if (qcounter==0) {
                            timeMarkers[marker]=String.valueOf(processesToRun[0]);
                            marker=marker+1;
                            timeMarkers[marker]=timeStamp;
                            marker=marker+1;
                        }
                        if ((qcounter+1)==timeQuantum) {
                            timeMarkers[marker]=timeStamp;
                            marker=marker+1;
                        }
                        else if(Integer.parseInt(data[processesToRun[0]][1])-1 == 0){
                            timeMarkers[marker]=timeStamp;
                            marker=marker+1;
                        }
    
                        System.out.println(timeStamp + "\tRunning Process " + data[processesToRun[0]][0] + "...");
                        num = Integer.parseInt(data[processesToRun[0]][1]);
                        temp = num - 1;
                        data[processesToRun[0]][1] = String.valueOf(temp);  
                        if (Integer.parseInt(data[processesToRun[0]][1]) == 0) {
                            pcounter++;
                            completionTime[ctHolder]= processesToRun[0];
                            ctHolder=ctHolder + 1;
                            completionTime[ctHolder]=timerCounter;
                            ctHolder=ctHolder + 1;
                        }
                        qcounter++;
                        timerCounter++;
    
                        Thread.sleep(1000);
                    }
                }
    
                    
            }
    
        }
    
        timer.cancel();
    
        System.out.println();
        System.out.println("\t\t\t\tSummary");
        System.out.println("========================================================================");
        System.out.println();
    
        for(int x = 0; x<numberOfProcesses; x++){
            for(int y = 0; y<100 ; y++){
                if(timeMarkers[y].equals(String.valueOf(x))){
                    lesserDur++;
                }
            }
            if (mostDur<lesserDur) {
                mostDur=lesserDur;
                lesserDur=0;
            }
        }
        
        System.out.println("\t\t\tTime Duration\t\t");
    
        
    
    
            for(int i=0; i<numberOfProcesses; i++){
                lesserDur=mostDur;
                System.out.print(data[i][0] + "\t");
                for(int j = 0; j<100; j++){
                    if(timeMarkers[j].equals(String.valueOf(i))){
                        if (lesserDur==mostDur) {
                            System.out.print(timeMarkers[j+1] + "-" + timeMarkers[j+2]);
                            lesserDur = lesserDur - 1;
                        }
                        else{
                            System.out.print(",\t");
                            System.out.print(timeMarkers[j+1] + "-" + timeMarkers[j+2]);
                            lesserDur = lesserDur - 1;
                        }
                        
                    }
                }

                System.out.println();
                
            }
    
            System.out.println();
            System.out.println("------------------------------------------------------------------------");
            System.out.println();
            System.out.println("\t\t\tWaiting Time\t\t\tTurnaround Time");
    
            for(int i=0; i<numberOfProcesses; i++){
                System.out.print(data[i][0] + "\t");
                for(int l = 0; l < (numberOfProcesses*2); l=l+2){
                    if(completionTime[l]==i){
                        if(burstTime[completionTime[l]]==0){
                            System.out.print("\t\t"+ 0);
                        }
                        else{
                            System.out.print("\t\t"+ (((completionTime[l+1])-(Integer.parseInt(data[completionTime[l]][2]))-burstTime[completionTime[l]])+1));
                            avgWT = avgWT + (((completionTime[l+1])-(Integer.parseInt(data[completionTime[l]][2]))-burstTime[completionTime[l]])+1);
                        }    
                        
                    }
                }
                for(int k = 0; k < (numberOfProcesses*2); k=k+2){
                    if(completionTime[k]==i){
                        if (burstTime[completionTime[k]]==0) {
                            System.out.print("\t\t\t\t"+ 0);
                        }
                        else{
                            System.out.print("\t\t\t\t"+ ((completionTime[k+1])-(Integer.parseInt(data[completionTime[k]][2]))+1));
                            avgTaT = avgTaT + ((completionTime[k+1])-(Integer.parseInt(data[completionTime[k]][2]))+1);
                        }
                        
                    }
                }
                System.out.println();
                
            }
    
            System.out.println();
            avgTaT = avgTaT/numberOfProcesses;
            avgWT = avgWT/numberOfProcesses;
    
            System.out.println("Average Waiting Time: " + avgWT + " seconds");
            System.out.println("Average Turnaround Time: " + avgTaT + " seconds");
            System.out.println();
    }

    public static String chooseAlgorithm(){
        String chosenAlgo = "0";

        System.out.println("____________________________________________");
        System.out.println("CPU Scheduling");
        System.out.println("____________________________________________");
        System.out.println("\nChoose an Algorithm:");
        System.out.println("A. Shortest-Job First");
        System.out.println("B. Priority Scheduling");
        System.out.println("C. Round Robin");
        System.out.println("\nEnter Choice (A/B/C): ");

        while((!chosenAlgo.equals("A"))&&(!chosenAlgo.equals("B"))&&(!chosenAlgo.equals("C")))
        {   
            chosenAlgo = read.nextLine();

            if(chosenAlgo.equals("a")){
                chosenAlgo = "A";
            }else if(chosenAlgo.equals("b")){
                chosenAlgo = "B";
            }else if(chosenAlgo.equals("c")){
                chosenAlgo = "C";
            }else if((!chosenAlgo.equals("A"))&&(!chosenAlgo.equals("B"))&&(!chosenAlgo.equals("C"))){
                System.out.println("Kindly enter a letter, A, B, or C only.");
            }
        }

        return chosenAlgo;
    }
}