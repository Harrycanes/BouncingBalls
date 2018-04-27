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
	
	final static double ELEMENT_WEIGHT = 2;
	final static double GRAVITY = 9.82; 
		
	
	Model(double width, double height) {
		areaWidth = width;
		areaHeight = height;
		
		
		// Initialize the model with a few balls
		balls = ballGenerator(3);
	}
	//random values
	Ball[] ballGenerator(int balls){
		Ball[] ballArray = new Ball[balls];
		ballArray[0] = new Ball(areaWidth/ 3, areaHeight * 0.9, 1.2, 1.6, 0.2);
		ballArray[1] = new Ball(2 * areaWidth / 3, areaHeight * 0.7, -0.6, 0.6, 0.25);
		for(int i=2;i<balls; i++){
			ballArray[i]= new Ball(rand.nextDouble()+1, rand.nextDouble()+1, rand.nextDouble()+1, 0, rand.nextDouble()/3);
		}
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
	void kinE(Ball bi, Ball bj) {
		double I = bi.mass*bi.trueVelocity+bj.mass*bj.trueVelocity;
		double R = -(bj.trueVelocity-bi.trueVelocity);
		bj.trueVelocity = I+(bi.mass*R)/(bi.mass+bj.mass);
		bi.trueVelocity = bj.trueVelocity-R;
	}

	void polarToRect(Ball bi, Ball bj) {
		bi.vx = Math.sin(bi.collisionAngle)*bi.trueVelocity;
		bj.vx = Math.sin(bi.collisionAngle)*bj.trueVelocity;
		
        bi.vy = Math.cos(bi.collisionAngle)*bi.trueVelocity;
        bj.vy = Math.cos(bi.collisionAngle)*bj.trueVelocity;
		
    
	}
	void rectToPolar(Ball bi, Ball bj) {
        bi.trueVelocity = Math.sqrt(bi.vx*bi.vx+bi.vy*bi.vy);
        bi.angle = Math.acos(bi.vx / bi.trueVelocity);
        
        bj.trueVelocity = Math.sqrt(bj.vx*bj.vx+bj.vy*bj.vy);
        bj.angle = Math.acos(bj.vx / bj.trueVelocity);
	}
	void unStuckBalls(Ball bi, Ball bj) {
        double x = Math.abs(bi.x - bj.x);
        double y = Math.abs(bi.y - bj.y);
        double dist = Math.sqrt(x*x + y*y);
        double overlap = Math.abs(dist-bi.radius-bj.radius);
        
        if(bi.x<bj.x){
        	bi.x =bi.x- overlap;//*Math.cos(bi.collisionAngle);
        	bj.x =bj.x+ overlap;//*Math.cos(bi.collisionAngle);
        }
        else {
        	bi.x =bi.x+ overlap;//*Math.cos(bi.collisionAngle);
        	bj.x =bj.x- overlap;//*Math.cos(bi.collisionAngle);
        }
        if(bi.y<bj.y){
        	bi.y =bi.y- overlap;//*Math.sin(bi.collisionAngle);
        	bj.y =bj.y+ overlap;//*Math.sin(bi.collisionAngle);
        }
        else {
        	bi.y =bi.y+ overlap;//*Math.sin(bi.collisionAngle);
        	bj.y =bj.y- overlap;//*Math.sin(bi.collisionAngle);
        }       
	}
	
	boolean collision(Ball bi, Ball bj){
        double x = Math.abs(bi.x - bj.x);
        double y = Math.abs(bi.y - bj.y);
        double dist = Math.sqrt(x*x + y*y);
        bi.collisionAngle = Math.acos(x/dist);
        return(dist <= (bi.radius + bj.radius));
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
			for(int j=i+1;j<balls.length;j++){
				Ball bj = balls[j];
				if(collision(bi,bj)){
					//kinEY(bi,bj);
					//kinEX(bi,bj);
					rectToPolar(bi,bj);
					kinE(bi,bj);
					polarToRect(bi,bj);	
					unStuckBalls(bi,bj);
				}
			}
		}

		for (Ball b : balls) {
			//Gravitation calculated
			double deltaG = b.mass*Model.GRAVITY;
			
			// compute new position according to the speed of the ball
			b.vy=b.vy;//-deltaG;
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
			this.mass = volume(r)*Model.ELEMENT_WEIGHT;
		}

		/**
		 * Position, speed, and radius of the ball. You may wish to add other attributes.
		 */
		double x, y, vx, vy, radius;
		final double mass;
		double angle,trueVelocity,collisionAngle;
	}
}