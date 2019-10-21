# 访问者模式（Vister Pattern）



在患者就医时，医生会根据病情开具处方单，很多医院都会存在以下这个流程：划价人员拿到处方单之后根据药品名称和数量计算总价，而药房工作人员根据药品名称和数量准备药品，如下图所示。

![](assets/5bb9884c94a36.png)



在软件开发中，有时候也需要处理像处方单这样的集合对象结构，在该对象结构中存储了多个不同类型的对象信息，而且对同一对象结构中的元素的操作方式并不唯一，可能需要提供多种不同的处理方式。在设计模式中，有一种模式可以满足上述要求，其模式动机就是以不同的方式操作复杂对象结构，该模式就是访问者模式。

```
Background：M公司开发部想要为某企业开发一个OA系统，在该OA系统中包含一个员工信息管理子系统，该企业包括正式员工和临时工，每周HR部门和财务部等部门需要对员工数据进行汇总，汇总数据包括员工工作时间、员工工资等等。该企业的基本制度如下：

（1）正式员工（Full time Employee）每周工作时间为40小时，不同级别、不同部门的员工每周基本工资不同；如果超过40小时，超出部分按照100元/小时作为加班费；如果少于40小时，所缺时间按照请假处理，请假锁扣工资以80元/小时计算，直到基本工资扣除到0为止。除了记录实际工作时间外，HR部需要记录加班时长或请假时长，作为员工平时表现的一项依据。

（2）临时员工（Part time Employee）每周工作时间不固定，基本工资按照小时计算，不同岗位的临时工小时工资不同。HR部只需要记录实际工作时间。

HR人力资源部和财务部工作人员可以根据各自的需要对员工数据进行汇总处理，HR人力资源部负责汇总每周员工工作时间，而财务部负责计算每周员工工资。
```

初始设计

```java
public  class EmployeeList
    {
        // 员工集合
        private IList<Employee> empList = new List<Employee>();

        // 增加员工
        public void AddEmployee(Employee emp)
        {
            this.empList.Add(emp);
        }

        // 处理员工数据
        public void Handle(string deptName)
        {
            if (deptName.Equals("财务部"))
            {
                foreach (var emp in empList)
                {
                    if (emp.GetType().Equals("FullTimeEmployee"))
                    {
                        Console.WriteLine("财务部处理全职员工数据！");
                    }
                    else
                    {
                        Console.WriteLine("财务部处理兼职员工数据！");
                    }
                }
            }
            else if (deptName.Equals("人力资源部"))
            {
                foreach (var emp in empList)
                {
                    if (emp.GetType().Equals("FullTimeEmployee"))
                    {
                        Console.WriteLine("人力资源部处理全职员工数据！");
                    }
                    else
                    {
                        Console.WriteLine("人力资源部处理兼职员工数据！");
                    }
                }
            }
        }
    }
```

　　不难发现，该解决方案存在以下问题：

　　（1）EmployeeList类非常庞大，承担了过多的职责，既不便于代码复用，也不便于系统扩展，违背了单一职责原则。

　　（2）包含了大量的if-else语句，测试和维护的难度增大。

　　（3）如果要新增一个部门来操作员工数据集合，那么不得不修改EmployeeList类的源代码，违背了开闭原则。

　　访问者模式是一个可以考虑用来解决的方案，它可以在一定程度上解决上述问题（大部分问题）。

## 模式简介

表示一个作用域某对象结构中的各个元素的操作。访问者模式让你可以在不改变各元素的类的前提下定义作用域这些元素的新操作。



　访问者模式结构图中包含以下5个角色：

　　（1）**Visitor（抽象访问者）**：抽象访问者为对象结构中每一个具体元素类ConcreteElement声明一个访问操作，从这个操作的名称或参数类型可以清楚知道需要访问的具体元素的类型，具体访问者则需要实现这些操作方法，定义对这些元素的访问操作。

　　（2）**ConcreteVisitor（具体访问者）**：具体访问者实现了抽象访问者声明的方法，每一个操作作用于访问对象结构中一种类型的元素。

　　（3）**Element（抽象元素）**：一般是一个抽象类或接口，定义一个Accept方法，该方法通常以一个抽象访问者作为参数。

　　（4）**ConcreteElement（具体元素）**：具体元素实现了Accept方法，在Accept方法中调用访问者的访问方法以便完成一个元素的操作。

　　（4）**ObjectStructure（对象结构）**：对象结构是一个元素的集合，用于存放元素对象，且提供便利其内部元素的方法。



（1）抽象元素=>IEmployee

```
    /// <summary>
    /// 抽象元素类：Employee
    /// </summary>
    public interface IEmployee
    {
        void Accept(Department handler);
    }
```

（2）具体元素=>FullTimeEmployee，PartTimeEmployee

```
 /// <summary>
    /// 具体元素类：FullTimeEmployee
    /// </summary>
    public class FullTimeEmployee : IEmployee
    {
        public string Name { get; set; }
        public double WeeklyWage { get; set; }
        public int WorkTime { get; set; }

        public FullTimeEmployee(string name, double weeklyWage, int workTime)
        {
            this.Name = name;
            this.WeeklyWage = weeklyWage;
            this.WorkTime = workTime;
        }

        public void Accept(Department handler)
        {
            handler.Visit(this);
        }
    }

    /// <summary>
    /// 具体元素类：PartTimeEmployee
    /// </summary>
    public class PartTimeEmployee : IEmployee
    {
        public string Name { get; set; }
        public double HourWage { get; set; }
        public int WorkTime { get; set; }

        public PartTimeEmployee(string name, double hourWage, int workTime)
        {
            this.Name = name;
            this.HourWage = hourWage;
            this.WorkTime = workTime;
        }

        public void Accept(Department handler)
        {
            handler.Visit(this);
        }
    }
```

（3）对象结构=>EmployeeList

```
/// <summary>
    /// 对象结构类：EmployeeList
    /// </summary>
    public class EmployeeList
    {
        private IList<IEmployee> empList = new List<IEmployee>();

        public void AddEmployee(IEmployee emp)
        {
            this.empList.Add(emp);
        }

        public void Accept(Department handler)
        {
            foreach (var emp in empList)
            {
                emp.Accept(handler);
            }
        }
```

　（4）抽象访问者=>Department

```
/// <summary>
    /// 抽象访问者类：Department
    /// </summary>
    public abstract class Department
    {
        // 声明一组重载的访问方法，用于访问不同类型的具体元素
        public abstract void Visit(FullTimeEmployee employee);
        public abstract void Visit(PartTimeEmployee employee);
    }
```

（5）具体访问者=>FinanceDepartment，HRDepartment

```
/// <summary>
    /// 具体访问者类：FinanceDepartment
    /// </summary>
    public class FinanceDepartment : Department
    {
        // 实现财务部对兼职员工数据的访问
        public override void Visit(PartTimeEmployee employee)
        {
            int workTime = employee.WorkTime;
            double hourWage = employee.HourWage;
            Console.WriteLine("临时工 {0} 实际工资为：{1} 元", employee.Name, workTime * hourWage);
        }

        // 实现财务部对全职员工数据的访问
        public override void Visit(FullTimeEmployee employee)
        {
            int workTime = employee.WorkTime;
            double weekWage = employee.WeeklyWage;

            if (workTime > 40)
            {
                weekWage = weekWage + (workTime - 40) * 50;
            }
            else if (workTime < 40)
            {
                weekWage = weekWage - (40 - workTime) * 80;
                if (weekWage < 0)
                {
                    weekWage = 0;
                }
            }

            Console.WriteLine("正式员工 {0} 实际工资为：{1} 元", employee.Name,  weekWage);
        }
    }

    /// <summary>
    /// 具体访问者类：HRDepartment
    /// </summary>
    public class HRDepartment : Department
    {
        // 实现人力资源部对兼职员工数据的访问
        public override void Visit(PartTimeEmployee employee)
        {
            int workTime = employee.WorkTime;
            Console.WriteLine("临时工 {0} 实际工作时间为：{1} 小时", employee.Name, workTime);
        }

        // 实现人力资源部对全职员工数据的访问
        public override void Visit(FullTimeEmployee employee)
        {
            int workTime = employee.WorkTime;
            Console.WriteLine("正式员工 {0} 实际工作时间为：{1} 小时", employee.Name, workTime);

            if (workTime > 40)
            {
                Console.WriteLine("正式员工 {0} 加班时间为：{1} 小时", employee.Name, workTime - 40);
            }
            else if (workTime < 40)
            {
                Console.WriteLine("正式员工 {0} 请假时间为：{1} 小时", employee.Name, 40 - workTime);
            }
        }
    }
```

（6）客户端调用与测试

```
public class Program
    {
        public static void Main(string[] args)
        {
            EmployeeList empList = new EmployeeList();
            IEmployee fteA = new FullTimeEmployee("梁思成", 3200.00, 45);
            IEmployee fteB = new FullTimeEmployee("徐志摩", 2000, 40);
            IEmployee fteC = new FullTimeEmployee("梁徽因", 2400, 38);
            IEmployee fteD = new PartTimeEmployee("方鸿渐", 80, 20);
            IEmployee fteE = new PartTimeEmployee("唐宛如", 60, 18);

            empList.AddEmployee(fteA);
            empList.AddEmployee(fteB);
            empList.AddEmployee(fteC);
            empList.AddEmployee(fteD);
            empList.AddEmployee(fteE);

            Department dept = AppConfigHelper.GetDeptInstance() as Department;
            if (dept != null)
            {
                empList.Accept(dept);
            }

            Console.ReadKey();
        }
    }
```

其中，AppConfigHelper用于从配置文件中获得具体访问者实例，配置文件如下：

```java
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <appSettings>
    <add key="DeptName" value="Manulife.ChengDu.DesignPattern.Visitor.HRDepartment, Manulife.ChengDu.DesignPattern.Visitor" />
  </appSettings>
</configuration>
```

　AppConfigHelper的具体代码如下：

```
public class AppConfigHelper
    {
        public static string GetDeptName()
        {
            string factoryName = null;
            try
            {
                factoryName = System.Configuration.ConfigurationManager.AppSettings["DeptName"];
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
            return factoryName;
        }

        public static object GetDeptInstance()
        {
            string assemblyName = AppConfigHelper.GetDeptName();
            Type type = Type.GetType(assemblyName);

            var instance = Activator.CreateInstance(type);
            return instance;
        }
    }
```

![img](assets/381412-20170730194807912-1199657618.png)



如果需要更换具体访问者类，无须修改源代码，只需要修改一下配置文件。例如这里将访问者由人力资源部更改为财务部：

```
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <appSettings>
    <add key="DeptName" value="Manulife.ChengDu.DesignPattern.Visitor.FinanceDepartment, Manulife.ChengDu.DesignPattern.Visitor" />
  </appSettings>
</configuration>
```

　　此时再次运行则会得到以下结果：

![img](assets/381412-20170730195025552-1986075012.png)

　　可以看出，如果我们要在系统中新增访问者，那么无需修改源代码，只需新增一个新的具体访问者类即可，从这一点看，访问者模式符合开闭原则。

　　但是，如果我们要在系统中新增具体元素，比如新增一个新的员工类型为“退休人员”，由于原系统并未提供相应的访问接口，因此必须对原有系统进行修改。所以，**从新增新的元素来看，访问者模式违背了开闭原则**。

　　因此，访问者模式与抽象工厂模式类似，对于开闭原则的支持具有“**倾斜**”性，可以方便地新增访问者，但是添加新的元素较为麻烦。

## 4.1 主要优点

　　（1）增加新的访问操作十分方便，不痛不痒 => **符合开闭原则**

　　（2）将有关元素对象的访问行为集中到一个访问者对象中，而不是分散在一个个的元素类中，类的职责更加清晰 => **符合单一职责原则**

## 4.2 主要缺点

　　（1）增加新的元素类很困难，需要在每一个访问者类中增加相应访问操作代码 => **违背了开闭原则**

　　（2）元素对象有时候必须暴露一些自己的内部操作和状态，否则无法供访问者访问 => **破坏了元素的封装性**

## 4.3 应用场景

　　（1）一个对象结构包含多个类型的对象，希望对这些对象实施一些依赖其具体类型的操作。=> 不同的类型可以有不同的访问操作

　　（2）对象结构中对象对应的类**很少改变 很少改变 很少改变（重要的事情说三遍）**，但经常需要在此对象结构上定义新的操作。