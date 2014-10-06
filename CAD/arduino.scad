//
// Holder:
//  1. Arduino Uno.
//  2. 2x Adafruit MotorShield v2.3
//  3. Bluefruit EZ-Link Shield.
//
// Print notes:
//  Shapeways strong and flexible, polished.
//  Printed fine.
//

$fn = 100;

use <pins.scad>;

// arduino mounts.
module mount_post(){
	/*
	difference(){
		union(){
			cylinder(r = 3, h = 5);
			cylinder(r = 1.5, h = 9);
			translate([0,0,7])
			cylinder(r1 = 2.0, r2 = 1.5, h = 2);
		}
		translate([0,0,7.01])
		cube(size = [4,0.8,4], center = true);
	}
	*/
	cylinder(r = 3, h = 4);
	cylinder(r = 1.5, h = 8);
}


// base plate.
module base_plate(){
	difference(){
		cube(size = [85, 60, 2]);

		translate([15,12,-0.5])
		hull(){
			cylinder(r = 2, h = 3);

			translate([55,0,0])
			cylinder(r = 2, h = 3);

			translate([0,36,0])
			cylinder(r = 2, h = 3);

			translate([55,36,0])
			cylinder(r = 2, h = 3);
		}

	}

	// arduino
	translate([23,5.9,1]){
		mount_post();
	}

	translate([24.3,54.1,1]){
		mount_post();
	}

	translate([75.1,11.0,1]){
		mount_post();
	}

	translate([75.1,38.9,1]){
		mount_post();
	}

	translate([-6,2,0])
	difference(){
		union(){
			cylinder(r = 4, h = 3.92);
			translate([0,-4,0])
			cube(size = [8, 8, 3.92]);
		}
		pin(bottom_rim_height = 1.0);
	}

	translate([-6,58,0])
	difference(){
		union(){
			cylinder(r = 4, h = 3.92);
			translate([0,-4,0])
			cube(size = [8, 8, 3.92]);
		}
		pin(bottom_rim_height = 1.0);
	}

	translate([90,2,0])
	difference(){
		union(){
			cylinder(r = 4, h = 3.92);
			translate([-8,-4,0])
			cube(size = [8, 8, 3.92]);
		}
		pin(bottom_rim_height = 1.0);
	}

	translate([90,58,0])
	difference(){
		union(){
			cylinder(r = 4, h = 3.92);
			translate([-8,-4,0])
			cube(size = [8, 8, 3.92]);
		}
		pin(bottom_rim_height = 1.0);
	}

	translate([0,-2,0])
	cube(size = [85,2,3.92]);

	translate([0,60,0])
	cube(size = [85,2,3.92]);
}

module end_plate(){
	difference(){
		cube(size = [45,60,2]);

		translate([0,0,-0.5]){
			hull(){
				translate([7,7,0])
				cylinder(r = 2, h = 3);

				translate([38,7,0])
				cylinder(r = 2, h = 3);

				translate([7,53,0])
				cylinder(r = 2, h = 3);

				translate([38,53,0])
				cylinder(r = 2, h = 3);

			}
		}
	}
}

module side_slot(){
	hull(){
		translate([2,7,0])
		cylinder(r = 2, h = 3);

		translate([17,7,0])
		cylinder(r = 2, h = 3);

		translate([2,38,0])
		cylinder(r = 2, h = 3);

		translate([17,38,0])
		cylinder(r = 2, h = 3);
	}
}

module side_plate(){
	difference(){
		cube(size = [85,45,2]);

		translate([7,0,-0.5]){
			side_slot();

			translate([26,0,0])
			side_slot();		

			translate([52,0,0])
			side_slot();		
		}
	}
}

module side_plate2(){
	difference(){
		cube(size = [85,45,2]);

		translate([7,7,-0.5]){
			for(i=[0:7]){
				hull(){
					translate([i*8,0,0])
					cylinder(r = 2, h = 3);

					translate([12 + i*8,31,0])
					cylinder(r = 2, h = 3);
				}
			}

			hull(){
				translate([0,20.6,0])
				cylinder(r = 2, h = 3);

				translate([4,31,0])
				cylinder(r = 2, h = 3);
			}

			hull(){
				translate([64,0,0])
				cylinder(r = 2, h = 3);

				translate([68,10.4,0])
				cylinder(r = 2, h = 3);
			}
		}
	}
}

// top plate.
module top_plate(){

	difference(){
		cube(size = [85, 60, 2]);

		translate([15,12,-0.5])
		hull(){
			cylinder(r = 2, h = 3);

			translate([55,0,0])
			cylinder(r = 2, h = 3);

			translate([0,36,0])
			cylinder(r = 2, h = 3);

			translate([55,36,0])
			cylinder(r = 2, h = 3);
		}

	}

	for(i=[0:6]){
		translate([7,4 + i*8,2])
		rotate([-25,0,0])
		cube(size = [71,4.5,2]);
	}

	translate([23,5.9,1]){
		rotate([180,0,0])
		mount_post();
	}

	translate([24.3,54.1,1]){
		rotate([180,0,0])
		mount_post();
	}

	translate([75.1,38.9,1]){
		rotate([180,0,0])
		mount_post();
	}
	
	translate([0,0,-44]){
		translate([85,0,0])
		rotate([90,0,180])
		side_plate2();

		translate([0,60,0])		
		rotate([90,0,0])
		side_plate2();

		translate([2,0,0])
		rotate([0,-90,0])
		end_plate();

		translate([85,0,0])
		rotate([0,-90,0])
		end_plate();

		translate([-6,2,4])
		rotate([180,0,0])
		difference(){
			union(){
				cylinder(r = 4, h = 3.92);
				translate([0,-4,0])
				cube(size = [8, 8, 3.92]);
			}
			pin(bottom_rim_height = 1.0);
		}

		translate([-6,58,4])
		rotate([180,0,0])
		difference(){
			union(){
				cylinder(r = 4, h = 3.92);
				translate([0,-4,0])
				cube(size = [8, 8, 3.92]);
			}
			pin(bottom_rim_height = 1.0);
		}

		translate([90,2,4])
		rotate([180,0,0])	
		difference(){
			union(){
				cylinder(r = 4, h = 3.92);
				translate([-8,-4,0])
				cube(size = [8, 8, 3.92]);
			}
			pin(bottom_rim_height = 1.0);
		}

		translate([90,58,4])
		rotate([180,0,0])
		difference(){
			union(){
				cylinder(r = 4, h = 3.92);
				translate([-8,-4,0])
				cube(size = [8, 8, 3.92]);
			}
			pin(bottom_rim_height = 1.0);
		}

		translate([0,-2,0])
		cube(size = [85,2,3.92]);

		translate([0,60,0])
		cube(size = [85,2,3.92]);
	}
}

if(0){
	base_plate();
	translate([0,0,48])
	top_plate();
}
else{
	base_plate();
	translate([0,65,44])
	top_plate();
}
