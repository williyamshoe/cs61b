public class Planet
{
  public double xxPos;
  public double yyPos;
  public double xxVel;
  public double yyVel;
  public double mass;
  public String imgFileName;
  public Planet(double xP, double yP, double xV, double yV, double m, String img)
  {
    xxPos = xP;
    yyPos = yP;
    xxVel = xV;
    yyVel = yV;
    mass = m;
    imgFileName = img;
  }

  public Planet(Planet p)
  {
    xxPos = p.xxPos;
    yyPos = p.yyPos;
    xxVel = p.xxVel;
    yyVel = p.yyVel;
    mass = p.mass;
    imgFileName = p.imgFileName;
  }

  public double calcDistance(Planet p)
  {
    return Math.pow(Math.pow(p.xxPos-xxPos, 2) + Math.pow(p.yyPos-yyPos, 2), 0.5);
  }

  public double calcForceExertedBy(Planet p)
  {
    return (6.67e-11)*this.mass*p.mass/Math.pow(this.calcDistance(p), 2);
  }

  public double calcForceExertedByX(Planet p)
  {
    return this.calcForceExertedBy(p)*(p.xxPos-this.xxPos)/this.calcDistance(p);
  }

  public double calcForceExertedByY(Planet p)
  {
    return this.calcForceExertedBy(p)*(p.yyPos-this.yyPos)/this.calcDistance(p);
  }

  public double calcNetForceExertedByX(Planet[] planets)
  {
    double total = 0;
    for (int i = 0; i < planets.length; i += 1)
    {
      if (! planets[i].equals(this))
      {
        total += this.calcForceExertedByX(planets[i]);
      }
    }
    return total;
  }

  public double calcNetForceExertedByY(Planet[] planets)
  {
    double total = 0;
    for (int i = 0; i < planets.length; i += 1)
    {
      if (! planets[i].equals(this))
      {
        total += this.calcForceExertedByY(planets[i]);
      }
    }
    return total;
  }

  public void update(double dt, double fX, double fY)
  {
    this.xxVel += dt * fX / this.mass;
    this.yyVel += dt * fY / this.mass;
    this.xxPos += dt * this.xxVel;
    this.yyPos += dt * this.yyVel;
  }

  public void draw()
  {
    StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
  }
}
