package com.example.jackiemun1.shoppinglist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jackiemun1.shoppinglist.adapter.ItemsAdapter;
import com.example.jackiemun1.shoppinglist.data.AppDatabase;
import com.example.jackiemun1.shoppinglist.data.Item;
import com.example.jackiemun1.shoppinglist.touch.ItemsListTouchHelperCallback;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CreateAndEditItemDialog.ItemHandler {

    public static final String KEY_EDIT = "KEY_EDIT";
    public static final String KEY_FIRST = "KEY_FIRST";

    private List<Item> items;
    private ItemsAdapter itemsAdapter;
    private CoordinatorLayout layoutContent;
    private RecyclerView recyclerViewItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutContent = findViewById(R.id.layoutContent);

        FloatingActionButton fabAdd = findViewById(R.id.addBtn);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateItemDialog();
            }
        });

        if (isFirstRun()) {
            new MaterialTapTargetPrompt.Builder(MainActivity.this)
                    .setTarget(findViewById(R.id.addBtn))
                    .setPrimaryText(R.string.newItemMsg)
                    .setSecondaryText(R.string.helperMsg)
                    .show();
        }

        setUpToolBar();

        recyclerViewItems = findViewById(R.id.recyclerViewItems);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));

        initItems(recyclerViewItems);

        saveThatItWasStarted();

    }

    public boolean isFirstRun() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                KEY_FIRST, true
        );
    }

    public void saveThatItWasStarted() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEY_FIRST, false);
        editor.commit();
    }

    private void initItems(final RecyclerView recyclerView) {
        new Thread() {
            @Override
            public void run() {
                items = AppDatabase.getAppDatabase(MainActivity.this).itemDao().getAll();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemsAdapter = new ItemsAdapter(items, MainActivity.this);
                        recyclerView.setAdapter(itemsAdapter);
                        setUpItemCount();

                        ItemsListTouchHelperCallback touchHelperCallback =
                                new ItemsListTouchHelperCallback(itemsAdapter);
                        ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
                        touchHelper.attachToRecyclerView(recyclerView);
                    }
                });
            }
        }.start();
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void setUpItemCount() {
        TextView itemCount = findViewById(R.id.item_count);
        itemCount.setText(getString(R.string.itemCount) + itemsAdapter.getItemCount());
    }

    private void showCreateItemDialog() {
        new CreateAndEditItemDialog().show(getFragmentManager(), getString(R.string.createItemTag));
    }

    public void showEditItemDialog(Item item) {
        CreateAndEditItemDialog editItemDialog = new CreateAndEditItemDialog();

        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_EDIT, item);
        editItemDialog.setArguments(bundle);

        editItemDialog.show(getFragmentManager(), getString(R.string.editItemTag));
        onItemUpdated(item);
    }

    private void showSnackBarMessage(String message) {
        Snackbar.make(layoutContent,
                message,
                Snackbar.LENGTH_LONG
        ).setAction(R.string.hideMsg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //...
            }
        }).show();
    }

    public void deleteAllItems() {
        recyclerViewItems.removeAllViews();
        itemsAdapter.deleteAll();
        itemsAdapter.notifyDataSetChanged();

        setUpItemCount();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                showCreateItemDialog();
                break;
            case R.id.action_delete_all:
                deleteAllItems();
                break;
            case R.id.action_total_spent:
                getTotal();
                break;
            case R.id.action_about:
                showSnackBarMessage(getString(R.string.aboutMsg));
                break;
        }
        return true;
    }

    public void getTotal() {
        double total = itemsAdapter.getTotalExpense();
        Toast.makeText(this, getString(R.string.totalExpenseMsg) + total,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNewItemCreated(final Item item) {
        new Thread(){
            @Override
            public void run() {
                long id = AppDatabase.getAppDatabase(MainActivity.this).
                        itemDao().insertItem(item);
                item.setItemID(id);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemsAdapter.addItem(item);
                        showSnackBarMessage(getString(R.string.itemAddedMsg));

                        setUpItemCount();
                    }
                });
            }
        }.start();
    }

    @Override
    public void onItemUpdated(final Item item) {
        new Thread() {
            @Override
            public void run() {
                AppDatabase.getAppDatabase(MainActivity.this).itemDao().update(item);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemsAdapter.updateItem(item);
                        setUpItemCount();
                    }
                });
            }
        }.start();
    }

}
