package s4.B201818; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/*package s4.umemura; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/* What is imported from s4.specification
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]); // set the data for computing the information quantities
    void setSpace(byte space[]); // set data for sample space to computer probability
    double estimation(); // It returns 0.0 when the target is not set or Target's length is zero;
// It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
// The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
// Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
// Otherwise, estimation of information quantity, 
}                        
*/

public class InformationEstimator implements InformationEstimatorInterface{
    // Code to tet, *warning: This code condtains intentional problem*
    byte [] myTarget; // data to compute its information quantity
    byte [] mySpace;  // Sample space to compute the probability
    FrequencerInterface myFrequencer;  // Object for counting frequency

    byte [] subBytes(byte [] x, int start, int end) {
	// corresponding to substring of String for  byte[] ,
	// It is not implement in class library because internal structure of byte[] requires copy.
	byte [] result = new byte[end - start];
	for(int i = 0; i<end - start; i++) { result[i] = x[start + i]; };
	return result;
    }

    // IQ: information quantity for a count,  -log2(count/sizeof(space))
    double iq(int freq) {
	return  - Math.log10((double) freq / (double) mySpace.length)/ Math.log10((double) 2.0);
    }

    public void setTarget(byte [] target) { myTarget = target;}
    public void setSpace(byte []space) { 
	myFrequencer = new Frequencer();
	mySpace = space; myFrequencer.setSpace(space); 
    }

    public double estimation(){
	boolean [] partition = new boolean[myTarget.length+1];
	int np;
	np = 1<<(myTarget.length-1);
	// System.out.println("np="+np+" length="+myTarget.length);
	double value = Double.MAX_VALUE; // value = mininimum of each "value1".

	for(int p=0; p<np; p++) { // There are 2^(n-1) kinds of partitions.
	    // binary representation of p forms partition.
	    // for partition {"ab" "cde" "fg"}
	    // a b c d e f g   : myTarget
	    // T F T F F T F T : partition:
	    partition[0] = true; // I know that this is not needed, but..
	    for(int i=0; i<myTarget.length -1;i++) {
		partition[i+1] = (0 !=((1<<i) & p));
	    }
	    partition[myTarget.length] = true;

	    // Compute Information Quantity for the partition, in "value1"
	    // value1 = IQ(#"ab")+IQ(#"cde")+IQ(#"fg") for the above example
            double value1 = (double) 0.0;
	    int end = 0;;
	    int start = end;
	    while(start<myTarget.length) {
		// System.out.write(myTarget[end]);
		end++;;
		while(partition[end] == false) { 
		    // System.out.write(myTarget[end]);
		    end++;
		}
		// System.out.print("("+start+","+end+")");
		myFrequencer.setTarget(subBytes(myTarget, start, end));
		value1 = value1 + iq(myFrequencer.frequency());
		start = end;
	    }
	    // System.out.println(" "+ value1);

	    // Get the minimal value in "value"
	    if(value1 < value) value = value1;
	}
	return value;
    }

    public static void main(String[] args) {
	InformationEstimator myObject;
	double value;
	myObject = new InformationEstimator();
	myObject.setSpace("3210321001230123".getBytes());
	myObject.setTarget("0".getBytes());
	value = myObject.estimation();
	System.out.println(">0 "+value);
	myObject.setTarget("01".getBytes());
	value = myObject.estimation();
	System.out.println(">01 "+value);
	myObject.setTarget("0123".getBytes());
	value = myObject.estimation();
	System.out.println(">0123 "+value);
	myObject.setTarget("00".getBytes());
	value = myObject.estimation();
	System.out.println(">00 "+value);
    }
}
				  
	

/*
interface FrequencerInterface {     // This interface provides the design for frequency counter.
    void setTarget(byte[]  target); // set the data to search.
    void setSpace(byte[]  space);  // set the data to be searched target from.
    int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
                    //Otherwise, it return 0, when SPACE is not set or Space's length is zero
                    //Otherwise, get the frequency of TAGET in SPACE
    int subByteFrequency(int start, int end);
    // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
    // For the incorrect value of START or END, the behavior is undefined.
}
*/

/*
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]); // set the data for computing the information quantities
    void setSpace(byte space[]); // set data for sample space to computer probability
    double estimation(); // It returns 0.0 when the target is not set or Target's length is zero;
// It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
// The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
// Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
// Otherwise, estimation of information quantity, 
}                        
*/
/*

public class TestCase {
    static boolean success = true;

    public static void main(String[] args) {
	try {
	    FrequencerInterface  myObject;
	    int freq;
	    System.out.println("checking Frequencer");

	    //スモークテスト
	    myObject = new Frequencer();
	    myObject.setSpace("Hi Ho Hi Ho".getBytes());
	    myObject.setTarget("H".getBytes());
	    freq = myObject.frequency();
	    assert freq == 4 : "Hi Ho Hi Ho, H: " + freq;
		
	    myObject = new Frequencer();
	    myObject.setSpace("Hi Ho H".getBytes());
	    myObject.setTarget("H".getBytes());
	    freq = myObject.frequency();
	    assert freq == 3 : "Hi Ho H, H: " + freq;
		
	    myObject = new Frequencer();
	    myObject.setSpace("Hi Ho Hi Ho".getBytes());
	    myObject.setTarget("Hi Ho".getBytes());
	    freq = myObject.frequency();
	    assert freq == 2 : "Hi Ho Hi Ho, Hi Ho: " + freq;
		
	    myObject = new Frequencer();
	    myObject.setSpace("Hi Ho Hi Ho".getBytes());
	    myObject.setTarget("Hi Ho H".getBytes());
	    freq = myObject.frequency();
	    assert freq == 1 : "Hi Ho Hi Ho, Hi Ho H: " + freq;
		
	    myObject = new Frequencer();
	    myObject.setSpace("Hi Ho Hi Ho".getBytes());
	    myObject.setTarget("Hi Ho Hi Ho Hi Ho".getBytes());
	    freq = myObject.frequency();
	    assert freq == 0 : "Hi Ho Hi Ho, Hi Ho Hi Ho Hi Ho: " + freq;
		
	    // 空のターゲットでテスト
	    myObject.setTarget("".getBytes());
	    freq = myObject.frequency();
	    assert freq == -1 : "空のターゲットは-1を返すべきですが、返り値は " + freq;

 	    // 空のスペースでテスト
 	    myObject.setTarget("H".getBytes());
 	    myObject.setSpace("".getBytes());
 	    freq = myObject.frequency();
 	    assert freq == 0 : "空のスペースは0を返すべきですが、返り値は " + freq;
 
	    // ターゲットとスペースがどちらも空の場合のテスト
	    myObject.setTarget("".getBytes());
	    myObject.setSpace("".getBytes());
	    freq = myObject.frequency();
	    assert freq == -1 : "空のターゲットとスペースは-1を返すべきですが、返り値は " + freq;

	    // スペースに存在しないターゲットでのテスト
	    myObject.setTarget("Z".getBytes());
	    myObject.setSpace("Hi Ho Hi Ho".getBytes());
	    freq = myObject.frequency();
	    assert freq == 0 : "スペースに存在しないターゲットは0を返すべきですが、返り値は " + freq;

	    // subByteFrequency メソッドのテスト //(0,1)を(1,2)に変更
	    myObject.setTarget("Hi".getBytes());
	    freq = myObject.subByteFrequency(1, 2);
	    assert freq == 2 : "'Hi Ho Hi Ho' の中で 'Hi' の subByteFrequency は start=1 かつ end=2 の場合に2を返すべきですが、返り値は " + freq;

	    //ターゲットの長さがスペースを超えている際のテスト
            myObject.setSpace("Hi Ho Hi Ho".getBytes());
            myObject.setTarget("GoodMorningWorld".getBytes());
	    freq = myObject.frequency();
	    assert freq == 0 : "ターゲットがスペースより長い場合は0を返すべきですが、返り値は " + freq;

	} catch (ArrayIndexOutOfBoundsException e) {
    	// This is the expected behavior, the test has passed
    		System.out.println("husei na index de array ni access");
	} 
	catch(Exception e) {
	    System.out.println("Exception occurred in Frequencer Object");
	    success = false;
	}

	try {
	    InformationEstimatorInterface myObject;
	    double value;
	    System.out.println("checking InformationEstimator");
	    myObject = new InformationEstimator();
	    myObject.setSpace("3210321001230123".getBytes());
	    myObject.setTarget("0".getBytes());
	    value = myObject.estimation();
	    assert (value > 1.9999) && (2.0001 >value): "IQ for 0 in 3210321001230123 should be 2.0. But it returns "+value;
	    myObject.setTarget("01".getBytes());
	    value = myObject.estimation();
	    assert (value > 2.9999) && (3.0001 >value): "IQ for 01 in 3210321001230123 should be 3.0. But it returns "+value;
	    myObject.setTarget("0123".getBytes());
	    value = myObject.estimation();
	    assert (value > 2.9999) && (3.0001 >value): "IQ for 0123 in 3210321001230123 should be 3.0. But it returns "+value;
	    myObject.setTarget("00".getBytes());
	    value = myObject.estimation();
	    assert (value > 3.9999) && (4.0001 >value): "IQ for 00 in 3210321001230123 should be 3.0. But it returns "+value;
	}
	catch(Exception e) {
	    System.out.println("Exception occurred in InformationEstimator Object");
	    success = false;
	}
        if(success) { System.out.println("TestCase OK"); } 
    }
} 
*/
	    
