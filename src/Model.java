import java.util.Random;
/**
 * The physics model.
 * 
 * This class is where you should implement your bouncing balls model.
 * 
 * The code has intentionally been kept as simple as possible, but if you wish, you can improve the design.
 * 
 * @author Simon Robillard
 *
 */
class Model {

	double areaWidth, areaHeight;
	double gravity = 9.82;
	Random rand = new Random();
	Ball [] balls;
	
	final static int ELEMENT_WEIGHT = 10; 
	
	
	//random values

	Model(double width, double height) {
		areaWidth = width;
		areaHeight = height;
		
		
		// Initialize the model with a few balls
		balls = ballGenerator(2);
	}
	Ball[] ballGenerator(int balls){
		Ball[] ballArray = new Ball[balls];
		for(int i=0;i<balls; i++){
			ballArray[i]= new Ball(rand.nextDouble()+1, rand.nextDouble()+1, rand.nextDouble()+1, 0, rand.nextDouble()/3);
		}
		return ballArray;
	}
	void bounce(){
		
	}
	double volume(double r){
		return (r*r*r*(4/3)*Math.PI);
	}

	void step(double deltaT) {
		// TODO this method implements one step of simulation with a step deltaT
		

		for (Ball b : balls) {
			double deltaG = volume(b.radius)*Model.ELEMENT_WEIGHT;
			b.vy=b.vy-deltaG;
			// detect collision with the border
			if (b.x < b.radius || b.x > areaWidth - b.radius) {
				b.vx *= -1; // change direction of ball
			}

			if (b.y < b.radius || b.y > areaHeight - b.radius) {
				b.vy *= -1;
			}
			// compute new position according to the speed of the ball
			if(b.y > areaHeight-b.radius ){
				b.y = areaHeight-b.radius;
				b.vy=-1*Math.abs(b.vy);
			}
			b.x += deltaT * b.vx;
			b.y += deltaT * b.vy;

		}
	}
	
	/**
	 * Simple inner class describing balls.
	 */
	class Ball {
		
		Ball(double x, double y, double vx, double vy, double r) {
			this.x = x;
			this.y = y;
			this.vx = vx;
			this.vy = vy;
			this.radius = r;
		}

		/**
		 * Position, speed, and radius of the ball. You may wish to add other attributes.
		 */
		double x, y, vx, vy, radius;
	}
}