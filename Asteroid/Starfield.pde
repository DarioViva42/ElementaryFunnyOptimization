class Starfield {

  // Attributes ---------------------------------
  ArrayList<Star> stars;


  // Constructor --------------------------------
  Starfield(int starAmount) {

    stars = new ArrayList<Star>();
    for(int i = 0; i < starAmount; i++) {
    stars.add(new Star());
    }

  }


  // Methods ------------------------------------
  void show() {
    for(int i = 0; i < this.stars.size(); i++) {

      this.stars.get(i).show();
      this.stars.get(i).update();

      }

    }

  }
