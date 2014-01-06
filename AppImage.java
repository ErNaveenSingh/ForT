try {
ApplicationInfo app = this.getPackageManager().getApplicationInfo("com.facebook.katana", 0);

Drawable icon = getPackageManager().getApplicationIcon(app);
Common.LOG(""+icon.toString());
((Button)findViewById(R.id.root_loginButton)).setBackgroundDrawable(icon);

} catch (NameNotFoundException e) {
Toast toast = Toast.makeText(this, "error in getting icon", Toast.LENGTH_SHORT);
toast.show();
e.printStackTrace();
}