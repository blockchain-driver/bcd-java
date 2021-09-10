package bcd.java;

import io.nodle.substratesdk.SubstrateApiKt;
import io.nodle.substratesdk.account.InvalidAccount;
import io.nodle.substratesdk.rpc.SubstrateProvider;
import io.nodle.substratesdk.account.Wallet;
import io.nodle.substratesdk.types.AccountData;
import io.nodle.substratesdk.types.AccountInfo;
import io.nodle.substratesdk.types.RuntimeMetadata;
//import io.reactivex.rxjava3.annotations.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;


//@Slf4j
public class UsingSubstrateClientKotlin {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UsingSubstrateClientKotlin.class);

    public static void main(String[] args) {
        log.info("Starting ...");
        String rpcUrl = "ws://127.0.0.1:9944";
        var provider = new SubstrateProvider(rpcUrl);
        log.info("Connected to {}", rpcUrl);

        // https://github.com/NodleCode/substrate-client-kotlin/issues/5
        //var meta = provider.getMetadata().blockingGet(); //error: reference to getMetadata is ambiguous
        var meta = provider.getMetadata().blockingGet();
        log.info("Metadata {}", meta);

        var genHash = provider.getGenesisHash().blockingGet();
        log.info("GenesisHash() ", genHash);

        var specVersion = provider.getSpecVersion().blockingGet();
        log.info("getSpecVersion {}", specVersion);

        var transactionVersion = provider.getTransactionVersion().blockingGet();

        log.info("getTransactionVersion specVersion={}", transactionVersion); //OK

        //fun stage1_testBalance
        String mnemonic = "";
        Wallet wallet1 = null;
        try {
            wallet1 = new Wallet(mnemonic);
        } catch (InvalidAccount e) {
            e.printStackTrace();
        }
//        var balance1 = wallet1.getAccountInfo(provider).blockingGet();
//        https://github.com/NodleCode/substrate-client-kotlin/issues/6
//        cannot find symbol
//        var balance1 = wallet1.getAccountInfo(provider).blockingGet();
//                              ^
//        symbol:   method getAccountInfo(SubstrateProvider)

        // https://kotlinlang.org/docs/java-to-kotlin-interop.html#package-level-functions
        //var balance1 = wallet1.getAccountInfo(provider).blockingGet();

        // AccountInfo AccountData
        @io.reactivex.rxjava3.annotations.NonNull AccountInfo balance1 = SubstrateApiKt.getAccountInfo(wallet1,provider).blockingGet();
        //balance1.
                log.info("balance1 AccountInfo={}", balance1);

        Assert.assertThat(balance1.getData().getFree(),//.toLong()
                CoreMatchers.equalTo(1000000000000L));


//        // fun stage2_testSignTx
//        String aliceMnemonic = "";
//        String bobMnemonic = "";
//        var src = Wallet(aliceMnemonic);
//        var destWallet = Wallet(bobMnemonic);
//        var txhash = src.signTx(provider, destWallet, new Integer(10).toBigInteger()).blockingGet()
        log.info("Finish.");
    }


}
