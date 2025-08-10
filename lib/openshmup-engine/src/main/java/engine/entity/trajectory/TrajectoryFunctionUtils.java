package engine.entity.trajectory;
//DO NOT OPTIMIZE IMPORTS
import static java.lang.Math.*;

final public class TrajectoryFunctionUtils {

    final public static class MathFloatOverloads {
    //WARNING: DO NOT DELETE
        public static float sin(float x){
            return (float) Math.sin(x);
        }

        public static float cos(float x){
            return (float) Math.cos(x);
        }

        public static float tan(float x){
            return (float) Math.tan(x);
        }

        public static float asin(float x){
            return (float) Math.asin(x);
        }

        public static float acos(float x){
            return (float) Math.acos(x);
        }

        public static float atan(float x){
            return (float) Math.atan(x);
        }

        public static float log(float x){
            return (float) Math.log10(x);
        }

        public static float sqrt(float x){
            return (float) Math.sqrt(x);
        }

        public static float cbrt(float x){
            return (float) Math.cbrt(x);
        }

        public static float ceil(float x){
            return (float) Math.ceil(x);
        }

        public static float floor(float x){
            return (float) Math.floor(x);
        }

        public static float rint(float x){
            return (float) Math.rint(x);
        }

        public static float atan2(float y, float x){
            return (float) Math.atan2(y, x);
        }

        public static float pow(float x, float y){
            return (float) Math.pow(x,y);
        }

        public static float E = (float) Math.E;
        public static float PI = (float) Math.PI;
        public static float TAU = (float) Math.TAU;
    }
}
