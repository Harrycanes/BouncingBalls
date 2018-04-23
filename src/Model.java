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
	
	final static int ELEMENT_WEIGHT = 2; 
	
	
	//random values

	Model(double width, double height) {
		areaWidth = width;
		areaHeight = height;
		
		
		// Initialize the model with a few balls
		balls = ballGenerator(2);
	}
	Ball[] ballGenerator(int balls){
		Ball[] ballArray = new Ball[balls];
		ballArray[0] = new Ball(areaWidth/ 3, areaHeight * 0.9, 1.2, 1.6, 0.1);
		ballArray[1] = new Ball(2 * areaWidth / 3, areaHeight * 0.7, -0.6, 0.6, 0.2);
		/*for(int i=0;i<balls; i++){
			ballArray[i]= new Ball(rand.nextDouble()+1, rand.nextDouble()+1, rand.nextDouble()+1, 0, rand.nextDouble()/3);
		}*/
		return ballArray;
	}
	void collision(Ball bi, Ball bj){
		double dirX = bi.vx*bj.vx;
		double dirY = bi.vy*bj.vy;
		double posx1 = bi.x;
		double width = bi.x+(bi.radius*2);
		double posy1 = bi.y;
		double height = bi.y+(bi.radius*2);
		if (bj.x>posx1&&bj.x<width&&bj.y>posy1&&bj.y<height){
			if (dirX<0){
				bi.vx*=-1;
				bj.vx*=-1;
			}
			if (dirY<0){
				bi.vy*=-1;
				bj.vy*=-1;
			}
		}
	}
	double volume(double r){
		return (r*r*r*(4/3)*Math.PI);
	}

	void step(double deltaT) {
		// TODO this method implements one step of simulation with a step deltaT
		for(int i=0;i<balls.length;i++){
			Ball bi=balls[i];
			for(int j=i+1;j<balls.length;j++){
				Ball bj = balls[j];
				collision(bi,bj);
			}
		}

		for (Ball b : balls) {
			double deltaG = volume(b.radius)*Model.ELEMENT_WEIGHT*gravity;
			
			// compute new position according to the speed of the ball
			b.vy=b.vy-deltaG;
			b.x += deltaT * b.vx;
			b.y += deltaT * b.vy;
			
			// detect collision with the border
			if (b.x < b.radius || b.x > areaWidth - b.radius) {
				b.vx *= -0.98; // change direction of ball
			}

			if (b.y < b.radius || b.y > areaHeight - b.radius) {
				b.vy *= -0.98;
			}
			
			if(b.y > areaHeight-b.radius ){
				b.y = areaHeight-b.radius;
				b.vy= (-1*Math.abs(b.vy));
			}
			else if (b.y < b.radius ){
				b.y = b.radius;
				b.vy=Math.abs(b.vy);
			}
			if(b.x > areaWidth-b.radius ){
				b.x = areaWidth-b.radius;
				b.vx= (-1*Math.abs(b.vx));
			}
			else if (b.x < b.radius ){
				b.x = b.radius;
				b.vx=Math.abs(b.vx);
			}
			

			
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