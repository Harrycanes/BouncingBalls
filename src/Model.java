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
	Random rand = new Random();
	Ball [] balls;
	
	final static int ELEMENT_WEIGHT = 2;
	final static double GRAVITY = 9.82; 
	
	public enum Direction {
		NORTH, EAST, SOUTH, WEST, NOCOLLISION
	}
	
	
	Model(double width, double height) {
		areaWidth = width;
		areaHeight = height;
		
		
		// Initialize the model with a few balls
		balls = ballGenerator(2);
	}
	//random values
	Ball[] ballGenerator(int balls){
		Ball[] ballArray = new Ball[balls];
		ballArray[0] = new Ball(areaWidth/ 3, areaHeight * 0.9, 1.2, 1.6, 0.1);
		ballArray[1] = new Ball(2 * areaWidth / 3, areaHeight * 0.7, -0.6, 0.6, 0.2);
		/*for(int i=0;i<balls; i++){
			ballArray[i]= new Ball(rand.nextDouble()+1, rand.nextDouble()+1, rand.nextDouble()+1, 0, rand.nextDouble()/3);
		}*/
		return ballArray;
	}
	void kinEX(Ball bi, Ball bj) {
		double mi = bi.mass;
		double mj = bj.mass;
		double I = mi*bi.vx+mj*bj.vx;
		double R = -(bj.vx-bi.vx);
		bj.vx = I+(mi*R)/(mi+mj);
		bi.vx = bj.vx-R;
	}
	void kinEY(Ball bi, Ball bj) {
		double mi = bi.mass;
		double mj = bj.mass;
		double I = mi*bi.vy+mj*bj.vy;
		double R = -(bj.vy-bi.vy);
		bj.vy = I+(mi*R)/(mi+mj);
		bi.vy = bj.vy-R;
	}
	Direction collisionDetection(Ball bi, Ball bj) {
		if(bj.x > bi.x && bj.x < bi.x2 && bj.y < bi.y && bj.y > bi.y2) {
			return Direction.NORTH;
		}
		else if (bj.x2 > bi.x && bj.x2 < bi.x2 && bj.y < bi.y && bj.y > bi.y2) {
			return Direction.EAST;
		}
		else if (bj.x > bi.x && bj.x < bi.x2 && bj.y2 < bi.y && bj.y2 > bi.y2) {
			return Direction.SOUTH;
		}
		else if (bj.x2 > bi.x && bj.x2 < bi.x2 && bj.y2 < bi.y && bj.y2 > bi.y2) {
			return Direction.WEST;
		}
		else {
			return Direction.NOCOLLISION;
		}		
	}
	void collision(Ball bi, Ball bj){
		//Implementation of polar velocities makes this deprecated (redundant) ??
		switch(collisionDetection(bi,bj)) {
			case NORTH:
				kinEY(bi,bj);
				break;
			case SOUTH:
				kinEY(bi,bj);
				break;
			case WEST: 
				kinEX(bi,bj);
				break;
			case EAST:
				kinEX(bi,bj);
				break;
			case NOCOLLISION:
				break;
		}
	}
	//Ball's volume
	double volume(double r){
		return (r*r*r*(4/3)*Math.PI);
	}
	//Ball's area
	double area(double r){
		return (r*r*Math.PI);
	}

	void step(double deltaT) {
		// TODO this method implements one step of simulation with a step deltaT
		for(int i=0;i<balls.length;i++){
			Ball bi=balls[i];
			bi.x2 = bi.x+(bi.radius*2);
			bi.y2 = bi.y-(bi.radius*2);
			for(int j=i+1;j<balls.length;j++){
				Ball bj = balls[j];
				bj.x2 = bj.x+(bi.radius*2);
				bj.y2 = bj.y-(bi.radius*2);
				collision(bi,bj);
			}
		}

		for (Ball b : balls) {
			//Gravitation calculated
			double deltaG = b.mass*Model.GRAVITY;
			
			// compute new position according to the speed of the ball
			b.vy=b.vy-deltaG;
			b.x += deltaT * b.vx;
			b.y += deltaT * b.vy;
			
			// detect collision with the border
			if (b.x < b.radius || b.x > areaWidth - b.radius) {
				b.vx *= -0.99; // change direction of ball
			}

			if (b.y < b.radius || b.y > areaHeight - b.radius) {
				b.vy *= -0.99;
			}
			
			//So that the ball doesn't get stuck in border
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
			this.x2 = this.x-this.radius;
			this.y2 = this.y-this.radius;
			this.mass = volume(r)*Model.ELEMENT_WEIGHT;
		}

		/**
		 * Position, speed, and radius of the ball. You may wish to add other attributes.
		 */
		double x, y, vx, vy, radius;
		double x2,y2;
		final double mass;
	}
}