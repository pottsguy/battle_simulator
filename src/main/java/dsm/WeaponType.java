package dsm;

class WeaponType {
    WeaponName name;
    int damageMin;
    int damageMid;
    int damageMax;

    WeaponType(WeaponName name, int damageMin, int damageMid, int damageMax) {
        this.name = name;
        this.damageMin = damageMin;
        this.damageMid = damageMid;
        this.damageMax = damageMax;
    }
}