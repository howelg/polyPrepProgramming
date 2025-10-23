
public class ClosedFormFib
{
    public static void main(){
        double max = 30;
        for(double n=0; n<max; n++){
            double result = (1/Math.sqrt(5))*(Math.pow((1+Math.sqrt(5))/2,n)-Math.pow((1-Math.sqrt(5))/2,2));
            int cleanedresult = (int)(result + 1);
            System.out.println(cleanedresult);
        }
    }
}