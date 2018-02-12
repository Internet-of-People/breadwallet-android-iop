package global.iop.wallet.presenter.activities.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import global.iop.wallet.R;
import global.iop.wallet.presenter.activities.util.BRActivity;
import global.iop.wallet.presenter.customviews.BRDialogView;
import global.iop.wallet.tools.animation.BRAnimator;
import global.iop.wallet.tools.animation.BRDialog;
import global.iop.wallet.tools.manager.BRSharedPrefs;
import global.iop.wallet.tools.threads.BRExecutor;
import global.iop.wallet.tools.util.BRConstants;
import global.iop.wallet.wallet.BRPeerManager;


public class SyncBlockchainActivity extends BRActivity {
    private static final String TAG = SyncBlockchainActivity.class.getName();
    private Button scanButton;
    public static boolean appVisible = false;
    private static SyncBlockchainActivity app;

    public static SyncBlockchainActivity getApp() {
        return app;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_blockchain);

        ImageButton faq = (ImageButton) findViewById(R.id.faq_button);

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BRAnimator.isClickAllowed()) return;
                BRAnimator.showSupportFragment(app, BRConstants.reScan);
            }
        });

        scanButton = (Button) findViewById(R.id.button_scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BRAnimator.isClickAllowed()) return;
                BRDialog.showCustomDialog(SyncBlockchainActivity.this, getString(R.string.ReScan_alertTitle),
                        getString(R.string.ReScan_footer), getString(R.string.ReScan_alertAction), getString(R.string.Button_cancel),
                        new BRDialogView.BROnClickListener() {
                            @Override
                            public void onClick(BRDialogView brDialogView) {
                                brDialogView.dismissWithAnimation();
                                BRExecutor.getInstance().forLightWeightBackgroundTasks().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        BRSharedPrefs.putStartHeight(SyncBlockchainActivity.this, 0);
                                        BRSharedPrefs.putAllowSpend(SyncBlockchainActivity.this, false);
                                        BRPeerManager.getInstance().rescan();
                                        BRAnimator.startBreadActivity(SyncBlockchainActivity.this, false);

                                    }
                                });
                            }
                        }, new BRDialogView.BROnClickListener() {
                            @Override
                            public void onClick(BRDialogView brDialogView) {
                                brDialogView.dismissWithAnimation();
                            }
                        }, null, 0);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        appVisible = true;
        app = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        appVisible = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

}
