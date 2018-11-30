/**
 * 右侧导航栏
 */
$(function() {
	   var defaultData = [
		   {
               text: '核心舱',
               href: '#parent1',
               tags: ['6'],
               nodes: [
                   {
                       text: '空间应用系统',
                       href: '#child1',
                       tags: ['4'],
                       nodes: [
                           {
                               text: '应用信息主机01',
                               href: '#grandchild1',
                               tags: ['2'],
                               nodes: [
                            	   {
                            		   text: '内容1',
                            		   href: 'content1',
                            		   tags: ['0']
                            	   },
                            	   {
                            		   text: '内容2',
                            		   href: 'content2',
                            		   tags: ['0']
                            	   }
                               ]
                           },
                           {
                               text: '应用信息主机02',
                               href: '#grandchild2',
                               tags: ['0']
                           }
                       ]
                   },
                   {
                       text: '应用信息系统',
                       href: '#child2',
                       tags: ['0']
                   }
               ]
           },
           {
               text: '太空舱',
               href: '#parent2',
               tags: ['0']
           },
           {
               text: '自定义舱',
               href: '#parent3',
               tags: ['0']
           }
       ];
	   $('#treeview').treeview({
		   expandIcon: 'glyphicon glyphicon-chevron-right',
           collapseIcon: 'glyphicon glyphicon-chevron-down',
           nodeIcon: 'glyphicon glyphicon-bookmark',
           showBorder: false,
           data: defaultData
       });
   })