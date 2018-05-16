OAuth2 implement by WebView for Android
=======================================

### Gradle
```
implementation 'com.pananfly:oauth2:1.0'
```

### 1.Add OAuth2.xml file in your asserts dir with config your platforms

### 2.Init first before use , with below codes
```
OAuth2.init(context);
```

### 3.Implement your own platform
```
public class Example extends Platform {
    //your platform id in the xml
    private int id = 0;

    @Override
    public String getPlatformName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getPlatformId() {
        return id;
    }

    @Override
    public String getUrl(String appKey, String appSecret, String redirectUrl) {
        return "your url";
    }

    @Override
    public void authorizeResult(String result) {
        //autorize result, the result format maybe: "your redirect url" + "server response"
        //analyze result and do your own job
        //...
    }
}
```

### 4.Register OAuth2UI activity and apply internet permission in AndroidManifest.xml

### 5.Start authorize
```
Platform platform = new Example();
platform.setActionListener(mActionListener);
platform.autorize();
```
```
private PlatformActionListener mActionListener = new PlatformActionListener() {
    @Override
    public void onError(Platform platform, OAuth2Exception exception) {

    }

    @Override
    public void onComplete(Platform platform) {

    }

    @Override
    public void onCancel(Platform platform) {

    }
};
```

### 6.TO-DO
* Fix webview cookies of auto launch(by remove cookies?).
* Optimize callback between Platform and OAuth2UI.

License
-------

    Copyright 2018 pananfly

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.