import java.util.Arrays;

public class Record {

	private double[] attrList;
	public double[] getAttrList() {
		return attrList;
	}

	public Record(double[] attrList) {
		this.attrList = Arrays.copyOf(attrList, attrList.length);
	}
	
	public int numberOfAttributes(){
		return attrList.length;
	}

	@Override
	public String toString() {
		StringBuffer sBuffer = new StringBuffer("");
		for (double dub : this.attrList) {
			sBuffer.append(String.format("%.2f", dub) + ", ");
		}
		sBuffer.replace(sBuffer.length() - 2, sBuffer.length(), "");
		return sBuffer.toString();
	}
}