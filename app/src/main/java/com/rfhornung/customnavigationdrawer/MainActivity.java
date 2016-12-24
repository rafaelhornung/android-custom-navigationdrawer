package com.rfhornung.customnavigationdrawer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        LabelDialogFragment.LabelDialogListener
{
	private View contentMain;

	private DrawerLayout drawerLayout;

	private NavigationView navigationTop;

	private int currentIdSelected = -1;

	private ArrayList<NavigationItem> navigationItems;

	private static final String NAVIGATION_ITEMS = "navigationItems";

	private static final String CURRENT_ID_SELECTED = "navigationSelected";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		contentMain = findViewById(R.id.content_main);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		navigationTop = (NavigationView) findViewById(R.id.navigation_drawer);

		// click floating action button
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Snackbar.make(contentMain, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null)
				        .show();
			}
		});

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
		        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawerLayout.addDrawerListener(toggle);
		toggle.syncState();

		// listener to select items on top navigation drawer
		navigationTop.setNavigationItemSelectedListener(this);

		// listener to select items on botton navigation drawer
		NavigationView navigationBottom = (NavigationView) findViewById(R.id.navigation_drawer_bottom);
		navigationBottom.setNavigationItemSelectedListener(this);

		// remove scroll mode effect on botton navigation
		for (int i = 0; i < navigationBottom.getChildCount(); i++)
			navigationBottom.getChildAt(i).setOverScrollMode(View.OVER_SCROLL_NEVER);

		if (savedInstanceState != null && savedInstanceState.containsKey(NAVIGATION_ITEMS))
		{
			currentIdSelected = savedInstanceState.getInt(CURRENT_ID_SELECTED);
			navigationItems = savedInstanceState.getParcelableArrayList(NAVIGATION_ITEMS);
		}

		if (navigationItems == null)
			navigationItems = new ArrayList<>();

		// add items on navigation drawer
		for (NavigationItem item : navigationItems)
			addLabelMenuItem(item);

		// keep selected menu item on navigation drawer, default Notes
		currentIdSelected = currentIdSelected >= 0 ? currentIdSelected : R.id.nav_notes;
		navigationTop.getMenu().performIdentifierAction(currentIdSelected, 0);
		navigationTop.setCheckedItem(currentIdSelected);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		outState.putParcelableArrayList(NAVIGATION_ITEMS, navigationItems);
		outState.putInt(CURRENT_ID_SELECTED, currentIdSelected);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onBackPressed()
	{
		if (drawerLayout.isDrawerOpen(GravityCompat.START))
		{
			drawerLayout.closeDrawer(GravityCompat.START);
			return;
		}

		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings)
		{
			Snackbar.make(contentMain, "Action bar: " + item.getTitle(), Snackbar.LENGTH_LONG).show();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item)
	{
		// close drawer
		drawerLayout.closeDrawer(GravityCompat.START);

		// Handle navigation view item clicks here.
		int itemId = item.getItemId();

		if (itemId == R.id.nav_label_add)
		{
			// show dialog to add new Label
			new LabelDialogFragment().show(getSupportFragmentManager(), LabelDialogFragment.DIALOG_TAG);
			return true;
		}

		// current selected id
		currentIdSelected = itemId;

		if (itemId == R.id.nav_notes || item.getGroupId() == R.id.nav_group_label)
		{
			Snackbar.make(contentMain, "Navigation Item: " + item.getTitle() + " : " + item.getItemId(),
			        Snackbar.LENGTH_LONG).show();
		}

		return true;
	}

	@Override
	public void onLabelDialogDone(String text)
	{
		// create new item and add on list
		int id = navigationItems.size();
		NavigationItem navItem = new NavigationItem(id, text, R.drawable.ic_label_black_24dp, 0);
		navigationItems.add(navItem);

		// add new item on menu group label
		addLabelMenuItem(navItem);
	}

	private void addLabelMenuItem(NavigationItem navItem)
	{
		if (navigationTop == null || navigationTop.getMenu() == null)
			return;

		MenuItem menuItem = navigationTop.getMenu().findItem(R.id.nav_group_label);
		if (menuItem == null || menuItem.getSubMenu() == null)
			return;

		SubMenu subMenu = menuItem.getSubMenu();

		// add new item
		MenuItem newItem = subMenu.add(R.id.nav_group_label, navItem.getId(), 0, navItem.getTitle());
		newItem.setActionView(R.layout.menu_counter);
		newItem.setIcon(navItem.getIconId());
		newItem.setCheckable(true);
		newItem.setChecked(false);

		// set text on action view
		TextView view = (TextView) newItem.getActionView();
		view.setText(String.valueOf(navItem.getCount()));
	}
}
