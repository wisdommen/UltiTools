# 快速上手

### 快速上手

首先你需要在你的插件主类实例化一个PageRegister

{% hint style="info" %}
请勿修改 getPageRegister\(\) 方法名
{% endhint %}

```java
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin{

    private static PageRegister pageRegister;

    public static PageRegister getPageRegister() {
        return pageRegister;
    }

    @Override
    public void onEnable() {
        plugin = this;
        pageRegister = new PageRegister(plugin);
    }
}
```

使用这个API建立一个界面你会体会到无与伦比的快速与简单，你只需要四行代码即可建立一个可用的预设GUI界面。

```java
InventoryManager inventoryManager = new InventoryManager(null, 54, "我的第一个GUI", true);
inventoryManager.presetPage(ViewType.PREVIOUS_QUIT_NEXT);
inventoryManager.create();
ViewManager.registerView(inventoryManager, new Listener());
```

这样你就建立了一个没有持有者的54格的叫做“我的第一个GUI”的界面，并且他的下方有三个按钮，分别是“上一页”，“退出”和“下一页”，背景为灰色。

接下来编写监听器，即监听鼠标点击的物品的类。

监听器并非implement Bukkit自带的监听器接口，而是继承PagesListener。

```java
public class Listener extends PagesListener {
    @Override
    public void onItemClick(InventoryClickEvent event, Player player, InventoryManager inventoryManager, ItemStack clickedItem) {
        // do what you want to do.
    }
}
```

这样就可以了，无需在主类注册此事件，因为PagesListener已经帮你做好了，你只需要向上面那样注册就行

```java
ViewManager.registerView(inventoryManager, new Listener());
```

并且在Listener类的onItemClick中无需关心最下方菜单栏的点击事件，已经全部预处理好了。

至此，一个可以使用的GUI界面就做完了。

